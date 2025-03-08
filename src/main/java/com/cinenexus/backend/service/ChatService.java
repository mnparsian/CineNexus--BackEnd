package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.chat.ChatReactionDTO;
import com.cinenexus.backend.dto.chat.ChatRoomResponseDTO;
import com.cinenexus.backend.dto.chat.MessageMapper;
import com.cinenexus.backend.dto.chat.MessageResponseDTO;
import com.cinenexus.backend.enumeration.ChatRoomType;

import com.cinenexus.backend.enumeration.FriendshipStatusType;
import com.cinenexus.backend.model.chat.*;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.user.Friendship;

import com.cinenexus.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;
  private final ChatSeenRepository chatSeenRepository;
  private final ChatReactionRepository chatReactionRepository;
  private final FriendshipRepository friendshipRepository;
  private final ChatRequestRepository chatRequestRepository;

  public ChatService(
      ChatRoomRepository chatRoomRepository,
      ChatMessageRepository chatMessageRepository,
      UserRepository userRepository,
      ChatSeenRepository chatSeenRepository,
      ChatReactionRepository chatReactionRepository,
      FriendshipRepository friendshipRepository,
      ChatRequestRepository chatRequestRepository) {
    this.chatRoomRepository = chatRoomRepository;
    this.chatMessageRepository = chatMessageRepository;
    this.userRepository = userRepository;
    this.chatSeenRepository = chatSeenRepository;
    this.chatReactionRepository = chatReactionRepository;
    this.friendshipRepository = friendshipRepository;
    this.chatRequestRepository = chatRequestRepository;
  }

    public ChatRoomResponseDTO createChatRoom(Long creatorId, List<Long> userIds, ChatRoomType type, String name) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        List<User> participants = userRepository.findAllById(userIds);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCreatedBy(creator);
        chatRoom.setType(type);
        chatRoom.setName(name);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        for (User user : participants) {
            savedChatRoom.getParticipants().add(new UserChatRoom(user, savedChatRoom));
        }

        chatRoomRepository.save(savedChatRoom);

        // **پاسخ را فقط با اطلاعات ضروری برمی‌گردانیم تا از لوپ جلوگیری شود**
        return new ChatRoomResponseDTO(savedChatRoom.getId(), savedChatRoom.getName(), savedChatRoom.getType(), creator.getId());
    }




    public MessageResponseDTO sendMessage(Long chatRoomId, Long senderId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Message content cannot be empty");
        }
    User sender =
        userRepository
            .findById(senderId)
            .orElseThrow(() -> new RuntimeException("Sender not found"));
    ChatRoom chatRoom =
        chatRoomRepository
            .findById(chatRoomId)
            .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

    if (chatRoom.getType() == ChatRoomType.PRIVATE) {
      List<UserChatRoom> participants = chatRoom.getParticipants();
      if (participants.size() == 2) {
        User receiver =
            participants.stream()
                .map(UserChatRoom::getUser)
                .filter(user -> !user.getId().equals(senderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

          Optional<Friendship> friendship = friendshipRepository.findByUsers(sender, receiver);
          if (friendship.isEmpty() || friendship.get().getFriendshipStatus().getName() != FriendshipStatusType.ACCEPTED) {
              ChatRequest chatRequest = new ChatRequest(sender, receiver, LocalDateTime.now());
              chatRequestRepository.save(chatRequest);
              throw new RuntimeException("Chat request sent. Waiting for approval.");
          }

      }
    }

    ChatMessage message = new ChatMessage();
    message.setChatRoom(chatRoom);
    message.setSender(sender);
    message.setContent(content);
    message.setSentAt(LocalDateTime.now());
        System.out.println("Message Content: " + content);


        ChatMessage savedMessage = chatMessageRepository.save(message);
        return new MessageResponseDTO(
                savedMessage.getId(),
                chatRoomId,
                senderId,
                content,
                savedMessage.getSentAt(),
                savedMessage.getEditedAt(),
                savedMessage.getDeletedAt()
        );
  }

    public List<MessageResponseDTO> getMessages(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);
        return messages.stream()
                .map(MessageMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void markAsSeen(Long userId, Long chatRoomId, Long lastMessageId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    ChatRoom chatRoom =
        chatRoomRepository
            .findById(chatRoomId)
            .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    ChatMessage lastMessage =
        chatMessageRepository
            .findById(lastMessageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));

    chatSeenRepository
        .findByUserAndChatRoom(user, chatRoom)
        .ifPresentOrElse(
            chatSeen -> {
              chatSeen.setMessage(lastMessage);
              chatSeen.setSeenAt(LocalDateTime.now());
              chatSeenRepository.save(chatSeen);
            },
            () -> {
              ChatSeen chatSeen = new ChatSeen(user, lastMessage, LocalDateTime.now());
              chatSeenRepository.save(chatSeen);
            });
  }

  public ChatReactionDTO reactToMessage(Long userId, Long messageId, String reactionType) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    ChatMessage message =
        chatMessageRepository
            .findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));

    ChatRoom chatRoom = message.getChatRoom();
    boolean isMember =
        chatRoom.getParticipants().stream()
            .anyMatch(userChatRoom -> userChatRoom.getUser().getId().equals(userId));
    if (!isMember) {
      throw new RuntimeException("User is not a member of this chat room");
    }

      ChatReaction reaction = chatReactionRepository
        .findByUserAndMessage(user, message)
        .map(
            existingReaction -> {
              existingReaction.setReaction(reactionType);
              return chatReactionRepository.save(existingReaction);
            })
        .orElseGet(
            () -> {
              ChatReaction newReaction = new ChatReaction(user, message, reactionType);
              return chatReactionRepository.save(newReaction);
            });
      ChatReactionDTO reactionDTO = new ChatReactionDTO(
              reaction.getId(),
              reaction.getMessage().getId(),
              reaction.getUser().getId(),
              reaction.getReaction()
      );
      return reactionDTO;

  }

    // ✅ ویرایش پیام
    public MessageResponseDTO editMessage(Long messageId, Long userId, String newContent) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("You can only edit your own messages!");
        }

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new RuntimeException("Message content cannot be empty!");
        }

        message.setContent(newContent);
        message.setEditedAt(LocalDateTime.now());

        ChatMessage savedChat = chatMessageRepository.save(message);
        return new MessageResponseDTO(savedChat.getId(),savedChat.getChatRoom().getId(),savedChat.getSender().getId(),savedChat.getContent(),savedChat.getSentAt(),savedChat.getEditedAt(),savedChat.getDeletedAt());
    }

    // ✅ حذف پیام (Soft Delete)
    public void deleteMessage(Long messageId, Long userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own messages!");
        }

        message.setDeletedAt(LocalDateTime.now()); // علامت‌گذاری به عنوان حذف‌شده
        chatMessageRepository.save(message);
    }

    // ✅ ویرایش ری‌اکشن
    public ChatReactionDTO editReaction(Long reactionId, Long userId, String newReactionType) {
        ChatReaction reaction = chatReactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        if (!reaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only edit your own reactions!");
        }

        reaction.setReaction(newReactionType);
        ChatReaction savedReaction = chatReactionRepository.save(reaction);
        ChatReactionDTO reactionDTO = new ChatReactionDTO(
                reaction.getId(),
                reaction.getMessage().getId(),
                reaction.getUser().getId(),
                reaction.getReaction()
        );
        return reactionDTO;
    }

    // ✅ حذف ری‌اکشن
    public void deleteReaction(Long reactionId, Long userId) {
        ChatReaction reaction = chatReactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        if (!reaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reactions!");
        }

        chatReactionRepository.delete(reaction);
    }
}
