package com.Fisport.dto.response;

import com.Fisport.common.EBookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class BookingTimeResponse {
    private LocalTime startTime;
    private LocalTime endTime;
    private EBookingStatus bookingStatus;
}
