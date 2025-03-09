package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.subscription.SubscriptionResponseDTO;
import com.cinenexus.backend.dto.subscription.SubscriptionMapper;
import com.cinenexus.backend.enumeration.PaymentStatus;
import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.SubscriptionRepository;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private UserRepository userRepository;
    private SubscriptionMapper susbscriptionMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, SubscriptionMapper susbscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.susbscriptionMapper = susbscriptionMapper;
    }

    // دریافت همه اشتراک‌ها
    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(susbscriptionMapper::toDTO).toList();
    }

    // دریافت یک اشتراک خاص
    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Subscription not found"));
        return susbscriptionMapper.toDTO(subscription);
    }

    // دریافت اشتراک فعال یک کاربر
    public Optional<SubscriptionResponseDTO> getUserSubscription(Long userId) {
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE).map(susbscriptionMapper::toDTO);
    }

    // ایجاد اشتراک جدید برای تست (مثلاً بدون پرداخت)
    public SubscriptionResponseDTO createSubscription(Payment payment, SubscriptionType type) {
        User user = payment.getUser();

        // دریافت لیست اشتراک‌های کاربر
        List<Subscription> subscriptions = subscriptionRepository.findByUserOrderByStartDateDesc(user);

        // اگر لیست خالی باشد، یعنی کاربر هیچ اشتراکی ندارد
        if (subscriptions.isEmpty()) {
            return susbscriptionMapper.toDTO(createNewSubscription(user, payment, type)) ;
        }

        // دریافت آخرین اشتراک
        Subscription lastSubscription = subscriptions.get(0);

        // بررسی اینکه آیا اشتراک قبلی همچنان فعال است
        if (lastSubscription.getStatus() == SubscriptionStatus.ACTIVE) {
            System.out.println("⚠️ User " + user.getId() + " already has an active subscription. Skipping subscription creation.");
            return susbscriptionMapper.toDTO(lastSubscription) ;
        }

        // اگر اشتراک قبلی غیرفعال شده، یک اشتراک جدید ایجاد کن
        Subscription savedSubscription =  createNewSubscription(user, payment, type);
        return susbscriptionMapper.toDTO(savedSubscription);
    }

    // متد کمکی برای ایجاد اشتراک جدید
    private Subscription createNewSubscription(User user, Payment payment, SubscriptionType type) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPayment(payment);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1)); // یک ماهه
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setType(type);

        return subscriptionRepository.save(subscription);
    }



    // تمدید اشتراک
    public Optional<SubscriptionResponseDTO> renewSubscription(Long id) {
        return subscriptionRepository.findById(id).map(subscription -> {
            subscription.setEndDate(subscription.getEndDate().plusMonths(1));
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            return susbscriptionMapper.toDTO(savedSubscription);
        });
    }

    // لغو اشتراک
    public boolean cancelSubscription(Long id) {
        return subscriptionRepository.findById(id).map(subscription -> {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
            return true;
        }).orElse(false);
    }

    public String getSubscriptionStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<Subscription> subscription = subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE);

        return subscription.map(sub -> "Subscription is ACTIVE. Expires on: " + sub.getEndDate())
                .orElse("No active subscription found.");
    }



    @Scheduled(cron = "0 0 0 * * ?") // اجرای روزانه در نیمه‌شب
    public void deactivateExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByEndDateBeforeAndStatus(
                LocalDateTime.now(), SubscriptionStatus.ACTIVE);

        for (Subscription subscription : expiredSubscriptions) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
        }
    }

}
