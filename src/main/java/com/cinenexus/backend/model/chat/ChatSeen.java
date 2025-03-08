package com.cinenexus.backend.model.chat;

import com.cinenexus.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "chat_seen")
public class ChatSeen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    private LocalDateTime seenAt = LocalDateTime.now();

    public ChatSeen(User user, ChatMessage message, LocalDateTime seenAt) {
        this.user = user;
        this.message = message;
        this.seenAt = seenAt;
    }
}
