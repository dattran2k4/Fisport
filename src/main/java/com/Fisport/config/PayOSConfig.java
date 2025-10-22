package com.Fisport.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
@Getter
public class PayOSConfig {

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Value("${payos.return-url}")
    private String returnPaymentBookingSuccessUrl;

    @Value("${payos.cancel-url}")
    private String returnPaymentBooingCancelUrl;

    @Value("${payos.return-payment-wallet-url}")
    private String returnPaymentWalletSuccessUrl;

    @Value(("{payos.cancel-payment-wallet-cancel}"))
    private String returnPaymentWalletCancelUrl;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
