package com.Fisport.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.*;
import java.util.Set;

@Getter
public class BookingRequest {

    @NotNull(message = "Giờ bắt đầu không được trống")
    private LocalTime startTime;
    @NotNull
    private Integer duration;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private LocalDate date;
    @NotNull
    private Long subFieldId;
    @Valid
    private Set<BookingServiceItemRequest> bookingServiceItemRequest;
}
