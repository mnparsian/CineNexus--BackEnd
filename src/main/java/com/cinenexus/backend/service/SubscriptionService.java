package com.cinenexus.backend.service;

import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import com.cinenexus.backend.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // بررسی می‌کند که آیا کاربر اشتراک فعال دارد یا نه
    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.existsByUserIdAndEndDateAfter(userId, LocalDateTime.now());
    }

    // ایجاد یک اشتراک جدید بعد از پرداخت موفق
    public Subscription createSubscription(Payment payment, SubscriptionType type) {
        // بررسی کنیم که کاربر همزمان اشتراک فعال نداشته باشد
        if (hasActiveSubscription(payment.getUser().getId())) {
            throw new IllegalStateException("کاربر از قبل اشتراک فعال دارد!");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(payment.getUser());
        subscription.setPayment(payment);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1)); // فعلاً یک ماهه است
        subscription.setType(type);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(subscription);
    }

    // دریافت اشتراک کاربر
    public Optional<Subscription> getUserSubscription(Long userId) {
        return subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }

    // بررسی و تمدید اشتراک‌ها (مثلاً Cron Job برای بررسی زمان انقضا)
    public void checkAndExpireSubscriptions() {
        subscriptionRepository.findAllByStatus(SubscriptionStatus.ACTIVE).forEach(subscription -> {
            if (subscription.getEndDate().isBefore(LocalDateTime.now())) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(subscription);
            }
        });
    }
}
