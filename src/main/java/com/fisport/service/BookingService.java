package com.fisport.service;

import com.fisport.dto.request.BookingRequest;
import com.fisport.dto.response.*;
import com.fisport.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date);

    String createBooking(BookingRequest request, Long userId);

    List<BookingListResponse> getAllBookings(String name);

    void cancelBooking(Long id, String name);

    void markAsExpired(Long id);

    List<BookingForUserResponse> getBookingsForUser(String name);

    BookingForUserResponse getBookingForUser(Long id, String name);

    boolean isExpiredBooking(Booking booking);

    List<Integer> getAvailableDurationsBooking(Long id, LocalDate date, LocalTime startTime);

    List<SlotAvailableResponse> getAvailableSlots(Long id, LocalDate date);

    Booking findBooking(Long id);
}
