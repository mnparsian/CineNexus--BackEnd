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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private final UserChatRoomRepository userChatRoomRepository;
  private  final MessageMapper messageMapper;
  private final SimpMessagingTemplate messagingTemplate;

  public ChatService(
      ChatRoomRepository chatRoomRepository,
      ChatMessageRepository chatMessageRepository,
      UserRepository userRepository,
      ChatSeenRepository chatSeenRepository,
      ChatReactionRepository chatReactionRepository,
      FriendshipRepository friendshipRepository,
      ChatRequestRepository chatRequestRepository,
      UserChatRoomRepository userChatRoomRepository,
      MessageMapper messageMapper,
      SimpMessagingTemplate messagingTemplate) {
    this.chatRoomRepository = chatRoomRepository;
    this.chatMessageRepository = chatMessageRepository;
    this.userRepository = userRepository;
    this.chatSeenRepository = chatSeenRepository;
    this.chatReactionRepository = chatReactionRepository;
    this.friendshipRepository = friendshipRepository;
    this.chatRequestRepository = chatRequestRepository;
    this.userChatRoomRepository = userChatRoomRepository;
    this.messageMapper = messageMapper;
    this.messagingTemplate = messagingTemplate;
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


        return new ChatRoomResponseDTO(savedChatRoom.getId(), savedChatRoom.getName(), savedChatRoom.getType(), creator.getId());
    }



    public MessageResponseDTO sendMessage(Long chatRoomId, Long senderId, Long receiverId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Message content cannot be empty");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatRoom chatRoom;
        if (chatRoomId == null) {
            Optional<ChatRoom> existingRoom = chatRoomRepository.findPrivateChatRoomByUsers(sender, receiver);
            if (existingRoom.isPresent()) {
                chatRoom = existingRoom.get();
            } else {
                chatRoom = new ChatRoom();
                chatRoom.setType(ChatRoomType.PRIVATE);
                chatRoom.setName("PRIVATE_CHAT_" + sender.getId() + "_" + receiver.getId());
                chatRoom.setCreatedBy(sender);
                chatRoomRepository.save(chatRoom);

                userChatRoomRepository.save(new UserChatRoom(sender, chatRoom));
                userChatRoomRepository.save(new UserChatRoom(receiver, chatRoom));
            }
        } else {
            chatRoom = chatRoomRepository.findById(chatRoomId)
                    .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        }

        ChatMessage message = new ChatMessage(sender, chatRoom, content, LocalDateTime.now());
        ChatMessage savedMessage = chatMessageRepository.save(message);
        MessageResponseDTO dto = messageMapper.toDTO(savedMessage);


        List<UserChatRoom> participants = userChatRoomRepository.findUsersByChatRoom(chatRoom);
        for (UserChatRoom participant : participants) {
            messagingTemplate.convertAndSendToUser(
                    participant.getUser().getId().toString(),
                    "/queue/chat",
                    dto
            );
        }

        return dto;
    }
    public List<MessageResponseDTO> getPreviousMessages(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);
        return messages.stream().map(msg -> new MessageResponseDTO(
                msg.getId(),
                msg.getChatRoom().getId(),
                msg.getSender().getId(),
                msg.getContent(),
                msg.getSentAt(),
                msg.getEditedAt(),
                msg.getDeletedAt()
        )).collect(Collectors.toList());
    }
    public Long findChatRoomId(Long userId, Long friendId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findPrivateChatRoom(userId, friendId);
         if(chatRoom.isPresent()){
             return chatRoom.get().getId();
         }
         else{
             ChatRoom newChatRoom = new ChatRoom();
             newChatRoom.setName(STR."\{userId.toString()}And\{friendId.toString()}");
             newChatRoom.setType(ChatRoomType.PRIVATE);
             User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found"));
             newChatRoom.setCreatedBy(user);
             newChatRoom.setCreatedAt(LocalDateTime.now());
         return chatRoomRepository.save(newChatRoom).getId();
         }
    }




    public List<MessageResponseDTO> getMessages(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);
        return messages.stream()
                .map(messageMapper::toDTO)
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


    public void deleteMessage(Long messageId, Long userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own messages!");
        }

        message.setDeletedAt(LocalDateTime.now());
        chatMessageRepository.save(message);
    }


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


    public void deleteReaction(Long reactionId, Long userId) {
        ChatReaction reaction = chatReactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        if (!reaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reactions!");
        }

        chatReactionRepository.delete(reaction);
    }
}
