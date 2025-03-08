package com.cinenexus.backend.model.chat;

import com.cinenexus.backend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "chat_reactions")
public class ChatReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    @Column(nullable = false, length = 10)
    private String reaction; // ایموجی یا نوع ری‌اکشن

    public ChatReaction(User user, ChatMessage message, String reaction) {
        this.user = user;
        this.message = message;
        this.reaction = reaction;
    }
}
