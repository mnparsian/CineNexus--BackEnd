package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.chat.ChatSocket;
import com.cinenexus.backend.dto.chat.MessageResponseDTO;
import com.cinenexus.backend.model.chat.ChatMessage;
import com.cinenexus.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class ChatWebSocketController {
@Autowired
private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatSocket sendMessage(@Payload ChatSocket message) {
        System.out.println("📩 New message received!");
        chatService.sendMessage(message.getChatroomId(), message.getSenderId(),message.getReceiverId(), message.getContent());
        return message;
    }

    @SubscribeMapping("/topic/messages")
    public String handleSubscription() {
        System.out.println("👀 User connected to /topic/messages!");
        return "You are subscribed!";
    }

    @GetMapping("/chat/messages/{chatRoomId}")
    public ResponseEntity<List<MessageResponseDTO>> getChatMessages(@PathVariable Long chatRoomId) {
        List<MessageResponseDTO> messages = chatService.getPreviousMessages(chatRoomId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/chat/getChatRoom/{friendId}")
    public ResponseEntity<Map<String, Long>> getChatRoomId(@PathVariable Long friendId, @RequestParam Long userId) {
        Long chatRoomId = chatService.findChatRoomId(userId, friendId);
        if (chatRoomId != null) {
            return ResponseEntity.ok(Collections.singletonMap("chatRoomId", chatRoomId));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }






}
