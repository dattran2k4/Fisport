package com.Fisport.service.impl;

import com.Fisport.config.PayOSConfig;
import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import com.Fisport.service.PayOSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.WebhookData;

@RequiredArgsConstructor
@Service
public class PayOSServiceImpl implements PayOSService {

    private final PayOSConfig payOSConfig;
    private final PayOS payOS;

    @Override
    public String createPaymentLink(Booking booking) {

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

        String checkoutUrl = data.getCheckoutUrl();
        return checkoutUrl;
    }

    @Override
    public WebhookData verifyWebHook(Object body) {
        return payOS.webhooks().verify(body);
    }

    @Override
    public boolean comfirmWebHook(String paymentLink) {
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
                .name("Đặt sân trực tuyến Fisport")
                .quantity(1)
                .price(amount)
                .build();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toán đi mà")
                .returnUrl(payOSConfig.getReturnSuccessUrl())
                .cancelUrl(payOSConfig.getReturnCancelUrl())
                .item(item)
                .build();

        CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);

        String checkoutUrl = data.getCheckoutUrl();
        return checkoutUrl;
    }


}
