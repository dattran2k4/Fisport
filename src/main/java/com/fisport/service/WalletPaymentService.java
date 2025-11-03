package com.fisport.service;

import com.fisport.model.ChallengeParticipant;
import vn.payos.model.webhooks.WebhookData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletPaymentService {

    void handleVnPayReturn(Map<String, String> params);

    void handlePayOSReturn(WebhookData data);

    void payBooking(String paymentToken, String username);

    void refundBooking(Long bookingId);

    void payChallengeMatch(Long matchId, String username);

    void refund(Long fromUserId, Long toUserId, BigDecimal amount);


}
