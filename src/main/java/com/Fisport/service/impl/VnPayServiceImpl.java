package com.Fisport.service.impl;

import com.Fisport.config.VnPayConfig;
import com.Fisport.model.Booking;
import com.Fisport.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
@Service
public class VnPayServiceImpl implements VnPayService {
    private final VnPayConfig vnPayConfig;

    @Override
    public String createPaymentUrl(Booking booking, HttpServletRequest httpServletRequest) {
        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", vnPayConfig.getVnpTmnCode());
            vnpParams.put("vnp_Amount", booking.getTotalPrice().multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", booking.getId().toString());
            //Secure paymentToken
            vnpParams.put("vnp_OrderInfo", "Thanh toan booking: " + Base64.getEncoder().encodeToString(booking.getPaymentToken().getBytes(StandardCharsets.US_ASCII)));
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", buildReturnUrl(httpServletRequest));
            vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            vnpParams.put("vnp_IpAddr", getClientIp(httpServletRequest));

            // sắp xếp key alphabetically
            Map<String, String> sortedParams = new TreeMap<>(vnpParams);

            // tạo query string & hash
            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();
            for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");

                hashData.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }

            // remove trailing &
            if (query.length() > 0) query.deleteCharAt(query.length() - 1);
            if (hashData.length() > 0) hashData.deleteCharAt(hashData.length() - 1);

            //Chữ ký
            String vnpSecureHash = hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData.toString());

            //Ghép url
            String paymentUrl = vnPayConfig.getVnpPayUrl() + "?" + query.toString() + "&vnp_SecureHash=" + vnpSecureHash;

            return paymentUrl;

        } catch (Exception e) {
            throw new RuntimeException("Cannot create VNPay URL", e);
        }
    }

    /**
     * Validate secure hash trả về từ VNPay
     */
    @Override
    public boolean validatePayment(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get("vnp_SecureHash");
            if (vnpSecureHash == null) return false;

            Map<String, String> clone = new HashMap<>(params);
            clone.remove("vnp_SecureHash");
            clone.remove("vnp_SecureHashType");

            // sắp xếp key
            Map<String, String> sorted = new TreeMap<>(clone);

            StringBuilder hashData = new StringBuilder();
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                hashData.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            if (hashData.length() > 0) hashData.deleteCharAt(hashData.length() - 1);

            String calculatedHash = hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData.toString());
            return vnpSecureHash.equals(calculatedHash);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long extractBookingId(Map<String, String> params) {
        return Long.valueOf(params.get("vnp_TxnRef"));
    }

    /**
     * Lấy map params từ request
     */
    @Override
    public Map<String, String> getVnpayResponse(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> result = new HashMap<>();
        paramMap.forEach((k, v) -> result.put(k, v[0]));
        return result;
    }

    /**
     * Build return URL dựa trên context path
     */
    private String buildReturnUrl(HttpServletRequest request) {
        String context = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return context + vnPayConfig.getVnpReturnUrl();
    }

    /**
     * Tạo HMAC SHA512
     */
    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), "HmacSHA512");
        hmac.init(secretKey);
        byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.US_ASCII));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR"); // check proxy/nginx
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr(); // fallback
        }
        return ipAddress;
    }
}
