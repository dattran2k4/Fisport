package com.fisport.service;

import com.fisport.model.Booking;
import com.fisport.model.Payment;
import vn.payos.model.webhooks.WebhookData;

public interface PayOSService {
    String createPaymentLink(Booking booking);

    WebhookData verifyWebHook(Object body);

    boolean comfirmWebHook(String paymentLink);

    String createWalletPaymentLink(Payment payment);
}
