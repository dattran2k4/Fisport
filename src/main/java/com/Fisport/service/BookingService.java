package com.Fisport.service;

import com.Fisport.dto.response.BookingTimeResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date);

    void createBooking(BookingRequest request);
}
