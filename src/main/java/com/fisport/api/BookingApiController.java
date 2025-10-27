package com.fisport.api;

import com.fisport.dto.request.BookingRequest;
import com.fisport.dto.response.ApiResponse;
import com.fisport.dto.response.ResponseData;
import com.fisport.dto.response.ResponseError;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.BookingService;
import com.fisport.service.FieldHasTimeSlotService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingApiController {

    private final BookingService bookingService;
    private final FieldHasTimeSlotService fieldHasTimeSlotService;

    @GetMapping("/occupied")
    public ApiResponse getOccupiedSlots(@RequestParam @Min(1) Long subFieldId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.builder()
                .status(HttpStatus.FOUND.value())
                .message("occupied slots")
                .data(bookingService.getOccupiedSlots(subFieldId, date))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse createBooking(@Valid @RequestBody BookingRequest request, @AuthenticationPrincipal CustomUserDetails principal) {
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

    @GetMapping("/preview-timingPrice")
    public ApiResponse previewTimingPrice(@RequestParam Long fieldId, @RequestParam LocalTime startTime, @RequestParam LocalTime endTime) {
        BigDecimal price = fieldHasTimeSlotService.getTotalPriceSlotBooking(fieldId, startTime, endTime);

        return ApiResponse.builder()
                .status(200)
                .message("Giá giờ: ")
                .data(price)
                .build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseData<?> cancelBooking(@PathVariable Long id, Principal principal) {
        try {
            bookingService.cancelBooking(id, principal.getName());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Đã hủy booking T-T");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
