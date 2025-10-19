package com.Fisport.service;

import com.Fisport.common.EBookingStatus;
import com.Fisport.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookingExpiredScheduler {

    private final BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000) //miliseconds
    @Transactional
    public void expriePendingBookings() {
        int updated = bookingRepository.updateExpriedBooking(
                EBookingStatus.PENDING,
                EBookingStatus.FAILED,
                LocalDateTime.now());

        System.out.println("updated expired bookings: " + updated);
    }
}
