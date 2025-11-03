package com.fisport.api;

import com.fisport.dto.response.ApiResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Booking;
import com.fisport.model.Field;
import com.fisport.model.FieldType;
import com.fisport.model.SubField;
import com.fisport.repository.BookingRepository;
import com.fisport.service.ChallengeMatchTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/challenge-types")
public class ChallengeTypeApiController {

    private final BookingRepository bookingRepository;
    private final ChallengeMatchTypeService challengeMatchTypeService;

    @GetMapping
    public ApiResponse getAllChallengeTypes(@RequestParam(required = false) Long bookingId) {
        log.info("Get bookingId {}",  bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        SubField subField = booking.getSubfield();
        log.info("Get subfield {}",  subField);
        Field field = subField.getField();
        log.info("Get field {}",  field);
        FieldType fieldType = field.getFieldType();
        log.info("Get fieldType {}",  fieldType);
        Long id = booking.getSubfield().getField().getFieldType().getId();
        log.info("Get id {}",  id);

        return ApiResponse.builder()
                .status(200)
                .message("types")
                .data(challengeMatchTypeService.getAllChallengeMatchTypesByFieldType(id))
                .build();
    }
}
