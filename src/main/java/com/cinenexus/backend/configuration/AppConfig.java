package com.cinenexus.backend.configuration;

import com.cinenexus.backend.dto.payment.PaymentMapper;
import com.cinenexus.backend.model.payment.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean public PaymentMapper paymentMapper(){
        return new PaymentMapper();
    }
}
