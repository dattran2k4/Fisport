package com.Fisport.api;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.response.BookingTimeResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.model.Booking;
import com.Fisport.repository.BookingRepository;
import com.Fisport.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingApiController {

    private final BookingService bookingService;

    @GetMapping("/occupied")
    public ResponseData<?> getOccupiedSlots(Long subFieldId, LocalDate date) {

    }
}
