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
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    private LocalDateTime editedAt;
    private LocalDateTime deletedAt;

    public ChatMessage(User sender, ChatRoom chatRoom, String content, LocalDateTime sentAt) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.content = content;
        this.sentAt = sentAt;
    }
}

