package com.cinenexus.backend.model.payment;

import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
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
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // پرداخت مرتبط با این اشتراک

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type; // نوع اشتراک (ماهانه، سالانه و ...)

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status; // وضعیت اشتراک (ACTIVE, EXPIRED, PENDING_PAYMENT)
}
