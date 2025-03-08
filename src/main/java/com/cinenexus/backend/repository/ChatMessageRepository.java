package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    public List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
}
