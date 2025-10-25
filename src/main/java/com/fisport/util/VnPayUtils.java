package com.fisport.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class VnPayUtils {
    public static Map<String, String> buildHashAndQuery(Map<String, String> params) {
        // Sort alphabetically by key
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Encode & join using stream
        String hashData = fieldNames.stream()
                .filter(key -> params.get(key) != null && !params.get(key).isEmpty())
                .map(key -> key + "=" + encode(params.get(key)))
                .collect(Collectors.joining("&"));

        String query = fieldNames.stream()
                .filter(key -> params.get(key) != null && !params.get(key).isEmpty())
                .map(key -> encode(key) + "=" + encode(params.get(key)))
                .collect(Collectors.joining("&"));

        Map<String, String> result = new HashMap<>();
        result.put("hashData", hashData);
        result.put("query", query);
        return result;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.US_ASCII);
    }

    public static String generateTxnRefSimple() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }

    /**
     * Tạo HMAC SHA512
     */
    public static String hmacSHA512(String key, String data) throws Exception {
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
}
