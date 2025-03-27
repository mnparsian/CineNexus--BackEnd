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
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String name;
    private String surname;
    private String bio;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDate birthday;
    private List<String> socialLinks;
}
