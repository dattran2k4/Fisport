package com.Fisport.service;

import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.BookingDetailResponse;
import com.Fisport.dto.response.BookingForUserResponse;
import com.Fisport.dto.response.BookingListResponse;
import com.Fisport.dto.response.BookingTimeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date);

    String createBooking(BookingRequest request, Long userId);

    List<BookingListResponse> getAllBookings(String name);

    BookingDetailResponse getBooking(Long id, String name);

    void cancelBooking(Long id, String name);

    List<BookingForUserResponse> getBookingsForUser(String name);


}
