package com.Fisport.api;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.ApiResponse;
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
import org.springframework.format.annotation.DateTimeFormat;
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
    public ApiResponse<?> getOccupiedSlots(@RequestParam @Min(1) Long subFieldId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("occupied slots")
                .data(bookingService.getOccupiedSlots(subFieldId, date))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<?> createBooking(@Valid @RequestBody BookingRequest request, @AuthenticationPrincipal CustomUserDetails principal) {
        bookingService.createBooking(request, principal.getUser().getId());
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("booking created")
                .build();
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
