package com.cinenexus.backend.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class AuthTestController {

    @GetMapping("/jwt")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> checkJwt() {
        return ResponseEntity.ok("✅ JWT is valid and authentication works!");
    }
}
