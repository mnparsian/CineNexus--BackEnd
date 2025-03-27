package com.cinenexus.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAdminUpdateRequestDTO {
    private String password;
    private String role;
    private String status;
}
