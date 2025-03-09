package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    public Optional<Payment> findByPaypalPaymentId(String papalId);
}
