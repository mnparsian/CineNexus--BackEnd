package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.payment.PaymentResponseDTO;
import com.cinenexus.backend.enumeration.PaymentStatus;

import com.cinenexus.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ایجاد پرداخت جدید
    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestParam Long userId, @RequestParam Double amount) {
        PaymentResponseDTO payment = paymentService.createPayment(userId, amount);
        return ResponseEntity.ok(payment);
    }

    // دریافت لیست پرداخت‌ها
    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // دریافت پرداخت بر اساس ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long paymentId) {
        PaymentResponseDTO payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    // به‌روزرسانی وضعیت پرداخت
    @PutMapping("/update-status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(@RequestParam Long paymentId, @RequestParam PaymentStatus status) {
        PaymentResponseDTO updatedPayment = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(updatedPayment);
    }
}
