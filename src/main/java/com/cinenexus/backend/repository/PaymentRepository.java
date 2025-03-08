package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {}
