package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.SubscriptionStatus;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    public boolean existsByUserIdAndEndDateAfter(Long userId, LocalDateTime date);
    public Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
    public List<Subscription> findAllByStatus(SubscriptionStatus status);
    public Optional<Subscription> findByUserAndStatus(User user,SubscriptionStatus status);
    public List<Subscription> findByUserOrderByStartDateDesc(User ussr);

    List<Subscription> findByEndDateBeforeAndStatus(LocalDateTime now, SubscriptionStatus subscriptionStatus);
}
