package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.chat.ChatMessage;
import com.cinenexus.backend.model.chat.ChatReaction;
import com.cinenexus.backend.model.user.User;
import org.apache.logging.log4j.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatReactionRepository extends JpaRepository<ChatReaction,Long> {
    Optional<ChatReaction> findByUserIdAndMessageId(Long userId, Long messageId);
    Optional<ChatReaction> findByUserAndMessage(User user , ChatMessage message);

}
