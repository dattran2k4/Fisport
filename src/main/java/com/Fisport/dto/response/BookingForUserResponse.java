package com.Fisport.dto.response;

import com.Fisport.common.EBookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.*;

@Getter
@Builder
public class BookingForUserResponse {

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private BigDecimal totalPrice;
    private String fieldName;
    private String subFieldName;
    private String status;
    private Integer rating;
    private boolean cancel;
    private boolean canReview;
    private String paymentMethod;

}
