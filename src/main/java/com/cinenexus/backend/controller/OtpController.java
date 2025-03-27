package com.cinenexus.backend.controller;

import com.cinenexus.backend.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final EmailService emailService;

    public OtpController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        try {
            emailService.sendOtpEmail(email);
            return ResponseEntity.ok("A verification code has been sent to email " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isExpire = emailService.verifyOtp(email, otp);
        if (!isExpire) {
            emailService.deleteOtp(email);
            return ResponseEntity.ok("✅ The verification code is valid.");
        } else {
            return ResponseEntity.status(400).body("❌ The verification code is invalid or expired.");
        }
    }

}
