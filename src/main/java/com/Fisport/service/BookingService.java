package com.Fisport.service;

import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.BookingTimeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date);

    void createBooking(BookingRequest request, Long userId);
}
