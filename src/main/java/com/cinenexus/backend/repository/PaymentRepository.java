package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    public Optional<Payment> findByPaypalPaymentId(String papalId);
    public List<Payment> findAllByUser(User user);
}
