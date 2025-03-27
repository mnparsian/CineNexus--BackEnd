package com.cinenexus.backend.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequestDTO {
        private String username;
        @Email
        private String email;
        private String role;
        @NotBlank
        private String name;
        @NotBlank
        private String surname;
        private String bio;
        private String profileImage;
        @Past(message = "Birthday must be a past date")
        private LocalDate birthday;
        @NotNull
        private Long preferredLanguage_id;
        @NotNull
        private Long country_id;
        @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format")
        private String phoneNumber;
}
