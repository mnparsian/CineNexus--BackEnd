package com.cinenexus.backend.dto.user;

import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.model.user.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6,message = "Password must be at least 6 characters long")
    private String password;
    @NotBlank
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
