package com.cinenexus.backend.service;

import com.cinenexus.backend.enumeration.PaymentStatus;
import com.cinenexus.backend.enumeration.SubscriptionType;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.PaymentRepository;
import com.cinenexus.backend.repository.UserRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.paypal.base.Constants.CLIENT_ID;
import static com.paypal.base.Constants.CLIENT_SECRET;

@Service
public class PayPalService {

    private final APIContext apiContext;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    @Value("${paypal.api.base}")
    private String PAYPAL_API_BASE;
    @Value("${paypal.client.id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${paypal.client.secret}")
    private String PAYPAL_SECRET;

    public PayPalService(APIContext apiContext,
                         RestTemplate restTemplate,
                         UserRepository userRepository,
                         PaymentRepository paymentRepository,
                         SubscriptionService subscriptionService) {
        this.apiContext = apiContext;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionService = subscriptionService;
    }

    public Map<String, String> createPayment(Double amount, String currency, String description, String cancelUrl, String successUrl, UserDetails userDetails) throws PayPalRESTException {

        Amount paypalAmount = new Amount();
        paypalAmount.setCurrency(currency);
        paypalAmount.setTotal(String.format("%.2f", amount));


        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(paypalAmount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);


        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");


        com.paypal.api.payments.Payment paymentRequest = new com.paypal.api.payments.Payment();
        paymentRequest.setIntent("sale");
        paymentRequest.setPayer(payer);
        paymentRequest.setTransactions(transactions);


        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        paymentRequest.setRedirectUrls(redirectUrls);


        com.paypal.api.payments.Payment createdPayment = paymentRequest.create(apiContext);


        String paypalPaymentId = createdPayment.getId();

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        com.cinenexus.backend.model.payment.Payment paymentRecord = new com.cinenexus.backend.model.payment.Payment();
        paymentRecord.setPaypalPaymentId(paypalPaymentId);
        paymentRecord.setUser(user);
        paymentRecord.setAmount(amount);
        paymentRecord.setStatus(PaymentStatus.PENDING);
        paymentRecord.setPaymentDate(null);
        com.cinenexus.backend.model.payment.Payment savedPayment = paymentRepository.save(paymentRecord);
        String paymentId = savedPayment.getId().toString();


        String approvalLink = createdPayment.getLinks().stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                .getHref();

        Map<String, String> response = new HashMap<>();
        response.put("paymentId", paymentId);
        response.put("approvalLink", approvalLink);
        return response;
    }

    public Map<String, Object> executePayment(String paymentId, String payerId) {
        try {

            String accessToken = getAccessToken();
            System.out.println("🟢 Sending Payment Execution with Token: " + accessToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);


            Map<String, String> requestBody = Map.of("payer_id", payerId);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);


            ResponseEntity<Map> response = restTemplate.exchange(
                    PAYPAL_API_BASE + "/v1/payments/payment/" + paymentId + "/execute",
                    HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("🔍 PayPal Response: " + response);
                return response.getBody();
            } else {
                System.out.println("❌ Payment execution failed: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean completePayment(String paymentId, String token, String payerId) {
        try {

            Map<String, Object> paymentDetails = this.executePayment(paymentId, payerId);


            if (paymentDetails == null) {
                System.err.println("❌ PayPal response is NULL");
                return false;
            }


            System.out.println("🔍 PayPal Response: " + paymentDetails);


            Optional<com.cinenexus.backend.model.payment.Payment> optionalPayment =
                    paymentRepository.findByPaypalPaymentId(paymentId);

            if (optionalPayment.isEmpty()) {
                System.err.println("❌ Payment record not found in database for PayPal ID: " + paymentId);
                return false;
            }

            com.cinenexus.backend.model.payment.Payment paymentRecord = optionalPayment.get();


            String paymentState = (String) paymentDetails.get("state");
            if (paymentState == null || !"approved".equalsIgnoreCase(paymentState)) {
                System.err.println("❌ Payment state is not approved: " + paymentState);
                paymentRecord.setStatus(PaymentStatus.FAILED);
                paymentRecord.setPaymentDate(LocalDateTime.now());
                paymentRepository.save(paymentRecord);
                return false;
            }


            Map<String, Object> transactions = ((List<Map<String, Object>>) paymentDetails.get("transactions")).get(0);
            Map<String, Object> amountInfo = (Map<String, Object>) transactions.get("amount");

            if (amountInfo == null || amountInfo.get("total") == null) {
                System.err.println("❌ Amount info is missing in PayPal response");
                return false;
            }

            Double paidAmount = Double.parseDouble(amountInfo.get("total").toString());


            paymentRecord.setAmount(paidAmount);
            paymentRecord.setStatus(PaymentStatus.COMPLETED);
            paymentRecord.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(paymentRecord);


            subscriptionService.createSubscription(paymentRecord, SubscriptionType.PREMIUM);

            System.out.println("✅ Payment successfully completed for user: " + paymentRecord.getUser().getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getAccessToken() {
        String auth = Base64.getEncoder().encodeToString((PAYPAL_CLIENT_ID + ":" + PAYPAL_SECRET).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                PAYPAL_API_BASE + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        return (String) responseBody.get("access_token");
    }


}
