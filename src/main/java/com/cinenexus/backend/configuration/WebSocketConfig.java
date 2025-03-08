package com.cinenexus.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // ✅ To receive a message
        registry.setApplicationDestinationPrefixes("/app"); // ✅ WebSocket request routing
        System.out.println("🟢 WebSocket is ready to receive messages on `/app`!");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // ✅ WebSocket path
                .setAllowedOriginPatterns("*") // 🔥 Solving the CORS problem
                .withSockJS();
        System.out.println("✅ STOMP WebSocket registered in the `/ws` path!");
    }
}
