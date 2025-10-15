package com.Fisport.service;

import com.Fisport.common.EPaymentMethod;
import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.response.PaymentResponse;
import com.Fisport.model.Booking;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface PaymentService {
    String createPayment(PaymentRequest request, HttpServletRequest httpServletRequest);

    PaymentResponse handleVnpayReturn(Map<String, String> params);

    Booking findByPaymentToken(String paymentToken);
}
