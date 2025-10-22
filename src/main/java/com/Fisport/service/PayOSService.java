package com.Fisport.service;

import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import vn.payos.model.webhooks.WebhookData;

public interface PayOSService {
    String createPaymentLink(Booking booking);

    WebhookData verifyWebHook(Object body);

    boolean comfirmWebHook(String paymentLink);

    String createWalletPaymentLink(Payment payment);
}
