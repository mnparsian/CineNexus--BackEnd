package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.subscription.SubscriptionResponseDTO;
import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.repository.PaymentRepository;
import com.cinenexus.backend.service.PaymentService;
import com.cinenexus.backend.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  private final SubscriptionService subscriptionService;
  private final PaymentRepository paymentRepository;

  public SubscriptionController(
      SubscriptionService subscriptionService, PaymentRepository paymentRepository) {
    this.subscriptionService = subscriptionService;
    this.paymentRepository = paymentRepository;
  }
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<SubscriptionResponseDTO>> getAllSubscriptions() {
    return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
  }
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(@PathVariable Long id) {
    SubscriptionResponseDTO subscription = subscriptionService.getSubscriptionById(id);
    return ResponseEntity.ok(subscription);
  }
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/user/{userId}")
  public ResponseEntity<SubscriptionResponseDTO> getUserSubscription(@PathVariable Long userId) {
    Optional<SubscriptionResponseDTO> subscription =
        subscriptionService.getUserSubscription(userId);
    return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/create")
  public ResponseEntity<SubscriptionResponseDTO> createSubscription(
      @RequestParam Long paymentId, @RequestParam SubscriptionType type) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    SubscriptionResponseDTO newSubscription = subscriptionService.createSubscription(payment, type);
    return ResponseEntity.ok(newSubscription);
  }
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PutMapping("/renew/{id}")
  public ResponseEntity<SubscriptionResponseDTO> renewSubscription(@PathVariable Long id) {
    Optional<SubscriptionResponseDTO> renewedSubscription =
        subscriptionService.renewSubscription(id);
    return renewedSubscription
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/cancel/{id}")
  public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
    boolean canceled = subscriptionService.cancelSubscription(id);
    return canceled ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/status")
  public ResponseEntity<String> checkSubscriptionStatus(@RequestParam Long userId) {
    String statusMessage = subscriptionService.getSubscriptionStatus(userId);
    return ResponseEntity.ok(statusMessage);
  }


}
