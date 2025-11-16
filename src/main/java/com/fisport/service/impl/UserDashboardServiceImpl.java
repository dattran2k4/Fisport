package com.fisport.service.impl;

import com.fisport.common.EBookingStatus;
import com.fisport.common.EVoucherStatus;
import com.fisport.dto.response.UserDashboardResponse;
import com.fisport.model.Booking;
import com.fisport.model.User;
import com.fisport.model.Voucher;
import com.fisport.repository.BookingRepository;
import com.fisport.repository.UserRepository;
import com.fisport.repository.VoucherRepository;
import com.fisport.service.UserDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDashboardServiceImpl implements UserDashboardService {

    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final BookingRepository bookingRepository;

    @Override
    public UserDashboardResponse getData(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        List<Voucher> vouchers = voucherRepository.findByUsersUsername(username);
        int vouchersCount = (int) vouchers.stream().filter(voucher -> voucher.getStatus().equals(EVoucherStatus.ACTIVE)).count();

        List<Booking> bookings = bookingRepository.findByUserUsername(username);

        int pendingCount = (int) bookings.stream().filter(booking -> booking.getBookingStatus().equals(EBookingStatus.PENDING)).count();
        int completedCount = (int) bookings.stream().filter(booking -> booking.getBookingStatus().equals(EBookingStatus.COMPLETED)).count();

        BigDecimal totalSpent = bookings.stream()
                .map(Booking::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Integer, Long> bookingPerMonth = bookings.stream()
                .filter(b -> b.getBookingDate().getYear() == LocalDate.now().getYear())
                .collect(Collectors.groupingBy(b -> b.getBookingDate().getMonthValue(), Collectors.counting()));

        List<Long> monthlyBookings = new ArrayList<>();
        for (int i = 1; i <= 12; i++) monthlyBookings.add(bookingPerMonth.getOrDefault(i, 0L));

        return UserDashboardResponse.builder()
                .voucherCount(vouchersCount)
                .pendingCount(pendingCount)
                .completedCount(completedCount)
                .totalSpent(totalSpent)
                .monthlyBookings(monthlyBookings)
                .build();
    }
}
