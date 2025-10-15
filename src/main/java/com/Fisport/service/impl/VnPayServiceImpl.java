package com.Fisport.service.impl;

import com.Fisport.config.VnPayConfig;
import com.Fisport.model.Booking;
import com.Fisport.service.VnPayService;
import com.Fisport.util.VnPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

            String prefix = "BK";
            Long bookingId = booking.getId();
            Long timestamp = System.currentTimeMillis();
            // Dùng String.format
            String txnRef = String.format("%s%d-%d", prefix, bookingId, timestamp);
            vnpParams.put("vnp_TxnRef", txnRef);
            //Secure paymentToken
            vnpParams.put("vnp_OrderInfo", "Thanh toan booking: " + txnRef);
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", buildReturnUrl(httpServletRequest));
            vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            vnpParams.put("vnp_IpAddr", getClientIp(httpServletRequest));

            // sắp xếp key alphabetically
            Map<String, String> sortedParams = new TreeMap<>(vnpParams);

            Map<String, String> result = VnPayUtils.buildHashAndQuery(sortedParams);

            String hashData = result.get("hashData");
            String query = result.get("query");

            //Chữ ký
            String vnpSecureHash = VnPayUtils.hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData);

            //Ghép url
            String paymentUrl = vnPayConfig.getVnpPayUrl() + "?" + query + "&vnp_SecureHash=" + vnpSecureHash;

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

            Map<String, String> result = VnPayUtils.buildHashAndQuery(sorted);
            String hashData = result.get("hashData");


            String calculatedHash = VnPayUtils.hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData);
            return vnpSecureHash.equals(calculatedHash);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long extractBookingId(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");
        String withoutPrefix = txnRef.substring(2);
        return Long.parseLong(withoutPrefix.split("-")[0]);
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



    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR"); // check proxy/nginx
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr(); // fallback
        }
        return ipAddress;
    }
}
