package com.cinenexus.backend.model.chat;

import com.cinenexus.backend.enumeration.ChatRequestStatus;
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
@Table(name = "chat_requests")
public class ChatRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @ManyToOne
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiver;

  @Column(nullable = false)
  private LocalDateTime requestedAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ChatRequestStatus status;

  public ChatRequest(User sender, User receiver, LocalDateTime requestedAt) {
    this.sender = sender;
    this.receiver = receiver;
    this.requestedAt = requestedAt;
    this.status = ChatRequestStatus.PENDING;
  }
}
