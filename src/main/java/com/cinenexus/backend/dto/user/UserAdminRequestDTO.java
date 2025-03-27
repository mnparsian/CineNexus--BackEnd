package com.cinenexus.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAdminRequestDTO {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String role;
}
