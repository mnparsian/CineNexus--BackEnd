package com.cinenexus.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        logger.info("🟢 اتصال جدید به WebSocket برقرار شد!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("🔴 اتصال WebSocket قطع شد.");
    }
    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        System.out.println("✅ یک SUBSCRIBE جدید انجام شد: " + event);
    }

    @EventListener
    public void handleMessageEvent(SessionConnectEvent event) {
        System.out.println("🔵 یک پیام WebSocket دریافت شد: " + event);
    }

}
