package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.user.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.otp = :otp")
    Optional<Otp> findOtp(@Param("email") String email, @Param("otp") String otp);
    void deleteByEmail(String email);
}
