package com.Fisport.service.impl;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.response.BookingTimeResponse;
import com.Fisport.model.Booking;
import com.Fisport.repository.BookingRepository;
import com.Fisport.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<BookingTimeResponse> getOccupiedSlots(Long subFieldId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findOccupiedSlots(subFieldId, date, List.of(EBookingStatus.PENDING, EBookingStatus.PAID, EBookingStatus.COMPLETED)
        );
        return bookings.stream().map(b -> BookingTimeResponse.builder()
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .bookingStatus(b.getBookingStatus())
                .build()).toList();
    }


}
