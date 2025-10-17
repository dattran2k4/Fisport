package com.Fisport.service.impl;

import com.Fisport.config.PayOSConfig;
import com.Fisport.model.Booking;
import com.Fisport.service.PayOSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

@RequiredArgsConstructor
@Service
public class PayOSServiceImpl implements PayOSService {

    private final PayOSConfig payOSConfig;
    private final PayOS payOS;

    @Override
    public String createPaymentLink() {

        long orderCode = System.currentTimeMillis() / 1000;
        long amount = (long) (Math.random() * 100);

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Ten san pham")
                .quantity(1)
                .price(amount)
                .build();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("This is the description")
                .returnUrl(payOSConfig.getReturnSuccessUrl())
                .cancelUrl(payOSConfig.getReturnCancelUrl())
                .build();

        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);

        String checkoutUrl = data.getCheckoutUrl();
        return checkoutUrl;
    }
}
