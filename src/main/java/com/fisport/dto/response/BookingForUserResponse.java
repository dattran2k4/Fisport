package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.*;

@Getter
@Builder
public class BookingForUserResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private BigDecimal totalPrice;
    private String fieldBanner;
    private String fieldName;
    private String subFieldName;
    private String status;
    private Integer rating;
    private boolean cancel;
    private boolean canReview;
    private boolean canCreateMatch;
    private String paymentMethod;
    private String serviceItemName;
    private BigDecimal subPrice;
    private BigDecimal price;
}
