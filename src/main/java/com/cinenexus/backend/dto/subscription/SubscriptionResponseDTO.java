package com.cinenexus.backend.dto.subscription;

import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionResponseDTO {
    private Long id;
    private Long userId;
    private Long paymentId;
    private SubscriptionType subscriptionType;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
