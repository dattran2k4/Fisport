package com.fisport.api;

import com.fisport.dto.response.ApiResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Booking;
import com.fisport.repository.BookingRepository;
import com.fisport.service.ChallengeMatchTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/challenge-types")
public class ChallengeTypeApiController {

    private final BookingRepository bookingRepository;
    private final ChallengeMatchTypeService challengeMatchTypeService;

    @GetMapping
    public ApiResponse getAllChallengeTypes(@RequestParam(required = false) Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        Long id = booking.getSubfield().getField().getFieldType().getId();

        return ApiResponse.builder()
                .status(200)
                .message("types")
                .data(challengeMatchTypeService.getAllChallengeMatchTypesByFieldType(id))
                .build();
    }
}
