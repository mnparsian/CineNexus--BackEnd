package com.cinenexus.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserChangePasswordDTO {
    private Long userId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}
