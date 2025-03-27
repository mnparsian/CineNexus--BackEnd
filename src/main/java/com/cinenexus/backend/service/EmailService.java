package com.cinenexus.backend.service;



import com.cinenexus.backend.model.user.Otp;
import com.cinenexus.backend.repository.OtpRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
@Transactional
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final OtpRepository otpRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, OtpRepository otpRepository) {
        this.mailSender = mailSender;
        this.otpRepository = otpRepository;
    }


    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    public void sendOtpEmail(String toEmail) throws MessagingException {
        String otp = generateOTP();


        otpRepository.deleteByEmail(toEmail);


        Otp otpEntry = new Otp(toEmail, otp, LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntry);


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("CineNexus - Your verification code");
        helper.setText("<h3>Your verification code: <strong>" + otp + "</strong></h3>", true);

        mailSender.send(message);
    }

    // ✅ بررسی صحت OTP
    @Transactional
    public boolean verifyOtp(String email, String otp) {
    System.out.println("Email :"+ email);
    System.out.println("OTP:" + otp);
        Optional<Otp> findOtp = otpRepository.findOtp(email, otp);
        System.out.println("Find OTP: " + findOtp);
        Otp otpEntity = findOtp.orElseThrow(() -> new EntityNotFoundException("Otp not found"));
        return otpEntity.isExpired();
    }

    @Transactional
    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }


}
