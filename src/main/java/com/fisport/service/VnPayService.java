package com.fisport.service;

import com.fisport.model.Booking;
import com.fisport.model.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VnPayService {
    String createPaymentUrl(Booking booking, HttpServletRequest httpServletRequest);
    String createWalletPaymentUrl(Payment payment, HttpServletRequest httpServletRequest);
    boolean validatePayment(Map<String, String> params);
    Long extractId(Map<String, String> params);
    Map<String, String> getVnpayResponse(HttpServletRequest request);
}
