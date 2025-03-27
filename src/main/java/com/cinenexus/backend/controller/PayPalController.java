package com.cinenexus.backend.controller;



import com.cinenexus.backend.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    private final PayPalService payPalService;

    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam Double amount,@AuthenticationPrincipal UserDetails userDetails) {
        try {
            Map<String, String> approvalUrl = payPalService.createPayment(
                    amount,
                    "EUR",
                    "CineNexus Subscription Payment",
                    "http://localhost:5173/payment/failed",
                    "http://localhost:5173/payment/success",
                    userDetails
            );
            return ResponseEntity.ok(approvalUrl);
        } catch (PayPalRESTException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId
    ) {
        System.out.println("🔍 paymentId received: " + paymentId);
        System.out.println("🔍 payerId received: " + payerId);



        System.out.println("✅ Successful payment with Payment ID: " + paymentId);
        System.out.println("🟢 Token: " + token + " | Payer ID: " + payerId);
        payPalService.completePayment(paymentId,token,payerId);
        return ResponseEntity.ok("Payment Successful!");
    }


}

