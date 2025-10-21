package com.Fisport.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BookingRequest {

    @NotNull(message = "Giờ bắt đầu không được trống")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @NotNull(message = "Chọn thời gian chơi")
    private Integer duration;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @NotNull(message = "Chọn ngày chơi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull(message = "Chọn sân chơi")
    private Long subFieldId;
    @Valid
    private List<BookingServiceItemRequest> bookingServiceItemRequest = new ArrayList<>();
}
