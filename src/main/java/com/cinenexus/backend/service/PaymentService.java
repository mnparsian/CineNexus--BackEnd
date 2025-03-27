package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.payment.PaymentMapper;
import com.cinenexus.backend.dto.payment.PaymentResponseDTO;
import com.cinenexus.backend.enumeration.PaymentStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.PaymentRepository;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;
  private final PaymentMapper paymentMapper;
  private final SubscriptionService subscriptionService;
  private final PayPalService payPalService;

  public PaymentService(
      PaymentRepository paymentRepository,
      UserRepository userRepository,
      PaymentMapper paymentMapper,
      SubscriptionService subscriptionService,
      PayPalService payPalService) {
    this.paymentRepository = paymentRepository;
    this.userRepository = userRepository;
    this.paymentMapper = paymentMapper;
    this.subscriptionService = subscriptionService;
    this.payPalService = payPalService;
  }

  public PaymentResponseDTO createPayment(Long userId, Double amount) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Payment payment = new Payment();
    payment.setUser(user);
    payment.setAmount(amount);
    payment.setPaymentDate(LocalDateTime.now());
    payment.setStatus(PaymentStatus.PENDING);
    Payment savedPayment = paymentRepository.save(payment);
    return paymentMapper.convertToDTO(savedPayment);
  }

  // دریافت لیست همه پرداخت‌ها
  public List<PaymentResponseDTO> getAllPayments() {
    return paymentRepository.findAll().stream().map(paymentMapper::convertToDTO).toList();
  }

  // دریافت پرداخت بر اساس ID
  public PaymentResponseDTO getPaymentById(Long paymentId) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
    return paymentMapper.convertToDTO(payment);
  }

  public List<PaymentResponseDTO> getAllPaymentsByUserId(Long userId){
    User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User Not Found"));
    List<Payment> paymentList = paymentRepository.findAllByUser(user);
    return paymentList.stream().map(paymentMapper::convertToDTO).toList();
  }

  public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

    payment.setStatus(status);
    payment = paymentRepository.save(payment);


    if (status == PaymentStatus.COMPLETED && payment.getSubscription() == null) {
      subscriptionService.createSubscription(payment, SubscriptionType.PREMIUM);
    }

    return paymentMapper.convertToDTO(payment);
  }
}
