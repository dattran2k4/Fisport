package com.fisport.service.impl;

import com.fisport.config.PayOSConfig;
import com.fisport.model.Booking;
import com.fisport.model.Payment;
import com.fisport.service.PayOSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.WebhookData;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "PAYOS-SERVICE")
public class PayOSServiceImpl implements PayOSService {

    private final PayOSConfig payOSConfig;
    private final PayOS payOS;

    @Override
    public String createPaymentLink(Booking booking) {
        log.info("createPaymentLink");
        long timePart = System.currentTimeMillis() / 100;
        long orderCode = timePart * 100000 + booking.getId();
        long amount = booking.getTotalPrice().longValue();

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Đặt sân trực tuyến Fisport")
                .quantity(1)
                .price(amount)
                .build();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toán đi mà")
                .returnUrl(payOSConfig.getReturnPaymentBookingSuccessUrl())
                .cancelUrl(payOSConfig.getReturnPaymentBooingCancelUrl())
                .item(item)
                .build();

        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);
        log.info("created link payOS for Booking");
        return data.getCheckoutUrl();
    }

    @Override
    public WebhookData verifyWebHook(Object body) {
        log.info("verifyWebHook");
        return payOS.webhooks().verify(body);
    }

    @Override
    public boolean comfirmWebHook(String paymentLink) {
        log.info("comfirmWebHook");
        try {
            return payOS.webhooks().confirm(paymentLink) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String createWalletPaymentLink(Payment payment) {
        long timePart = System.currentTimeMillis() / 100;
        long orderCode = timePart * 100000 + payment.getId();
        long amount = payment.getAmount().longValue();

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Nạp zô ví:")
                .quantity(1)
                .price(amount)
                .build();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Nạp nạp nạp:")
                .returnUrl(payOSConfig.getReturnPaymentWalletSuccessUrl())
                .cancelUrl(payOSConfig.getReturnPaymentBooingCancelUrl())
                .item(item)
                .build();

        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);
        log.info("created link payOS for Wallet");
        return data.getCheckoutUrl();
    }


}
