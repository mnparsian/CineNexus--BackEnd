package com.cinenexus.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @MessageMapping("/chat/chat.sendMessage")
    @SendTo("/topic/messages")
    public void sendMessage() {
        System.out.println("🔥🔥🔥 The `sendMessage` method has been executed!");
    }


}
