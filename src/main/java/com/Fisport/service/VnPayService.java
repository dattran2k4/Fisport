package com.Fisport.service;

import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.model.Booking;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VnPayService {
    String createPaymentUrl(Booking booking, HttpServletRequest httpServletRequest);
    boolean validatePayment(Map<String, String> params);
    Long extractBookingId(Map<String, String> params);
    Map<String, String> getVnpayResponse(HttpServletRequest request);
}
