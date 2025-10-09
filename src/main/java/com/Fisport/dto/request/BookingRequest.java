package com.Fisport.dto.request;

import lombok.Getter;

import java.time.*;

@Getter
public class BookingRequest {


    private LocalTime startTime;
    private int duration;
    private LocalTime endTime;
    private LocalDate date;
    private Long subFieldId;
    private Long userId;

    private BookingServiceItemRequest;
}
