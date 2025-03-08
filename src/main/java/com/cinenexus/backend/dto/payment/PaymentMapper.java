package com.cinenexus.backend.dto.payment;

import com.cinenexus.backend.model.payment.Payment;

public class PaymentMapper {
  public PaymentResponseDTO convertToDTO(Payment payment) {
    return new PaymentResponseDTO(
        payment.getId(),
        payment.getUser().getId(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getPaymentDate(),
        payment.getSubscription());
        }
}
