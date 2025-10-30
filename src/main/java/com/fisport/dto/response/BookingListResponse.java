package com.fisport.dto.response;

import com.fisport.common.EBookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class BookingListResponse {
    private Long id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Integer duration;
    private String fieldName;
    private BigDecimal total;
    private EBookingStatus status;
}
