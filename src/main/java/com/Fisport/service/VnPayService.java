package com.Fisport.service;

import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.request.WalletTopUpRequest;
import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VnPayService {
    String createPaymentUrl(Booking booking, HttpServletRequest httpServletRequest);
    String createWalletPaymentUrl(Payment payment, HttpServletRequest httpServletRequest);
    boolean validatePayment(Map<String, String> params);
    Long extractId(Map<String, String> params);
    Map<String, String> getVnpayResponse(HttpServletRequest request);
}
