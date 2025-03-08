package com.cinenexus.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String name;
    private String surname;
    private String bio;
    private String profileImage;
    private Boolean isVerified;
    private String status; // ACTIVE, BANNED, DEACTIVATED
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDate birthday;
    private String preferredLanguage;
    private String country;
    private String phoneNumber;
    private List<String> socialLinks;

    // فقط آی‌دی‌ها برای کاهش حجم دیتا
    private List<Long> friendIds;
    private List<Long> favoriteMovieIds;
    private List<Long> watchlistIds;
    private List<Long> reviewIds;
    private List<Long> commentIds;
    private List<Long> subscriptionIds;
    private List<Long> paymentIds;
}
