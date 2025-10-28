package com.fisport.service;

import vn.payos.model.webhooks.WebhookData;

import java.util.Map;

public interface WalletPaymentService {

    void handleVnPayReturn(Map<String, String> params);

    void handlePayOSReturn(WebhookData data);

    void payBooking(String paymentToken, String username);

    void refundBooking(Long bookingId);

    void payChallengeMatch(Long matchId, String username);
}
