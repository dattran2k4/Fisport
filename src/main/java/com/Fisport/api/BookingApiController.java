package com.Fisport.api;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.BookingTimeResponse;
import com.Fisport.dto.response.ResponseData;
import com.Fisport.dto.response.ResponseError;
import com.Fisport.model.Booking;
import com.Fisport.repository.BookingRepository;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingApiController {

    private final BookingService bookingService;

    @GetMapping("/occupied")
    public ResponseData<?> getOccupiedSlots(@Min(1) Long subFieldId, LocalDate date) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "occupied", bookingService.getOccupiedSlots(subFieldId, date));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NO_CONTENT.value(), "get occupied fail");
        }
    }

    @PostMapping("/create")
    public ResponseData<?> createBooking(@Valid @RequestBody BookingRequest request, @AuthenticationPrincipal CustomUserDetails principal) {
        try {
            bookingService.createBooking(request, principal.getUser().getId());
            return new ResponseData<>(HttpStatus.OK.value(), "booking created successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "create booking fail");
        }
    }

    @GetMapping("/my")
    public ResponseData<?> getMyBookings(@AuthenticationPrincipal CustomUserDetails principal) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "my bookings successfully", bookingService.getAllBookings(principal.getUsername()));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "get my bookings fail");
        }
    }
}
