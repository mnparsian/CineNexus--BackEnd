package com.cinenexus.backend.dto.payment;

import com.cinenexus.backend.enumeration.PaymentStatus;
import com.cinenexus.backend.model.payment.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponseDTO {
    private Long id;
    private Long userId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private Subscription subscription;
}
