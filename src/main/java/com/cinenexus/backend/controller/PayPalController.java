package com.cinenexus.backend.controller;



import com.cinenexus.backend.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PayPalController {

    private final PayPalService payPalService;

    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestParam Double amount) {
        try {
            String approvalUrl = payPalService.createPayment(
                    amount,
                    "EUR",
                    "CineNexus Subscription Payment",
                    "http://localhost:8080/api/payments/cancel",
                    "http://localhost:8080/api/payments/success"
            );
            return ResponseEntity.ok(approvalUrl);
        } catch (PayPalRESTException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("paymentId") String paymentId,  // اینجا String باشه
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId
    ) {
        System.out.println("🔍 paymentId received: " + paymentId);
        System.out.println("🔍 payerId received: " + payerId);



        System.out.println("✅ پرداخت موفق با Payment ID: " + paymentId);
        System.out.println("🟢 Token: " + token + " | Payer ID: " + payerId);
        payPalService.completePayment(paymentId,token,payerId);
        // ادامه پردازش پرداخت...
        return ResponseEntity.ok("Payment Successful!");
    }


}

