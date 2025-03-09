package com.cinenexus.backend.dto.subscription;

import com.cinenexus.backend.model.payment.Subscription;

public class SubscriptionMapper {
    public  SubscriptionResponseDTO toDTO(Subscription subscription) {
        return new SubscriptionResponseDTO(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getPayment() != null ? subscription.getPayment().getId() : null,
                subscription.getType(),
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getEndDate()
        );
    }
}
