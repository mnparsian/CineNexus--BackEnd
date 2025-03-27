package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.chat.ChatReactionDTO;
import com.cinenexus.backend.dto.chat.ChatRoomResponseDTO;
import com.cinenexus.backend.dto.chat.MessageResponseDTO;
import com.cinenexus.backend.enumeration.ChatRoomType;
import com.cinenexus.backend.model.chat.*;
import com.cinenexus.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(@RequestBody Map<String, Object> payload) {
        Long creatorId = Long.valueOf((Integer) payload.get("creatorId"));
        List<Long> userIds = ((List<Integer>) payload.get("userIds")).stream()
                .map(Long::valueOf).toList();
        ChatRoomType type = ChatRoomType.valueOf((String) payload.get("type"));
        String name = (String) payload.get("name");

        ChatRoomResponseDTO chatRoom = chatService.createChatRoom(creatorId, userIds, type, name);
        return ResponseEntity.ok(chatRoom);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
        Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long recieverId = Long.valueOf(payload.get("receiverId").toString());
        String content = payload.get("content") != null ? payload.get("content").toString() : null;

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            MessageResponseDTO responseDTO = chatService.sendMessage(chatRoomId, senderId,recieverId, content);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getMessages(chatRoomId));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/seen")
    public ResponseEntity<?> markAsSeen(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
        Long lastMessageId = Long.valueOf(payload.get("lastMessageId").toString());

        chatService.markAsSeen(userId, chatRoomId, lastMessageId);
        return ResponseEntity.ok("Message seen updated");
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/reaction")
    public ResponseEntity<?> reactToMessage(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        String reactionType = payload.get("reactionType").toString();

        ChatReactionDTO reactionDTO = chatService.reactToMessage(userId, messageId, reactionType);
        return ResponseEntity.ok(reactionDTO);
    }



    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/message/edit")
    public ResponseEntity<MessageResponseDTO> editMessage(@RequestBody Map<String, Object> payload) {
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        String newContent = payload.get("newContent").toString();

        MessageResponseDTO updatedMessage = chatService.editMessage(messageId, userId, newContent);
        return ResponseEntity.ok(updatedMessage);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/message/delete")
    public ResponseEntity<String> deleteMessage(@RequestBody Map<String, Object> payload) {
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());

        chatService.deleteMessage(messageId, userId);
        return ResponseEntity.ok("Message deleted successfully.");
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/reaction/edit")
    public ResponseEntity<?> editReaction(@RequestBody Map<String, Object> payload) {
        Long reactionId = Long.valueOf(payload.get("reactionId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        String newReactionType = payload.get("newReactionType").toString();

        ChatReactionDTO updatedReaction = chatService.editReaction(reactionId, userId, newReactionType);
        return ResponseEntity.ok(updatedReaction);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/reaction/delete")
    public ResponseEntity<String> deleteReaction(@RequestBody Map<String, Object> payload) {
        Long reactionId = Long.valueOf(payload.get("reactionId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());

        chatService.deleteReaction(reactionId, userId);
        return ResponseEntity.ok("Reaction deleted successfully.");
    }
}
