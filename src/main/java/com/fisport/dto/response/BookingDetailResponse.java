package com.fisport.dto.response;

import com.fisport.common.EBookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class BookingDetailResponse {
    private Long id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Integer duration;
    private String fieldName;
    private String subFieldName;
    private EBookingStatus bookingStatus;
    private BigDecimal total;
    private Set<BookingServiceItemResponse> items = new HashSet<>();
    private LocalDateTime createdAt;
    private PaymentResponse payment;
}
