package com.fisport.api;

import com.fisport.dto.response.ApiResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Booking;
import com.fisport.model.Field;
import com.fisport.model.FieldType;
import com.fisport.model.SubField;
import com.fisport.repository.BookingRepository;
import com.fisport.service.BookingService;
import com.fisport.service.ChallengeMatchTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j(topic = "MATCH-TYPE-API-CONTROLLER")
@RequestMapping("/api/v1/challenge-types")
public class ChallengeTypeApiController {

    private final BookingService bookingService;
    private final ChallengeMatchTypeService challengeMatchTypeService;

    @GetMapping
    public ApiResponse getAllChallengeTypes(@RequestParam(required = false) Long bookingId, @RequestParam(required = false) Long fieldTypeId) {
        Long id = null;

        if (bookingId != null) {
            Booking booking = bookingService.findBooking(bookingId);
            id = booking.getSubfield().getField().getFieldType().getId();
        } else if (fieldTypeId != null) {
            id =  fieldTypeId;
        }

        return ApiResponse.builder()
                .status(200)
                .message("types")
                .data(challengeMatchTypeService.getAllChallengeMatchTypesByFieldType(id))
                .build();
    }
}
