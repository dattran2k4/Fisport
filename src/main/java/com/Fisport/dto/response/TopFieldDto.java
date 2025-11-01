package com.Fisport.dto.response;

public record TopFieldDto(Long fieldId, String fieldName, Long bookings, java.math.BigDecimal revenue, Double rating) {}
