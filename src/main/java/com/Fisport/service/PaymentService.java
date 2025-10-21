package com.Fisport.service;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.response.PaymentResponse;
import com.Fisport.model.Booking;
import jakarta.servlet.http.HttpServletRequest;
import vn.payos.model.webhooks.WebhookData;

import java.util.Map;

public interface PaymentService {
    String createPayment(PaymentRequest request, HttpServletRequest httpServletRequest);

    PaymentResponse handleVnpayReturn(Map<String, String> params);

    void handlePayOSWebHook(WebhookData data);

    Booking findByPaymentToken(String paymentToken);

    Booking findByOrderCodePayOs(long orderCode);

    PaymentResponse checkPaymentPayOSView(long orderCode, String status);
}
