package com.Fisport.controller.admin;

import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.FieldRepository;
import com.Fisport.repository.PaymentRepository;
import com.Fisport.repository.ReviewRepository;
import com.Fisport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(com.Fisport.common.EUserStatus.ACTIVE);
        long newUsersThisMonth = userRepository.countCreatedAfter(LocalDateTime.now().minusMonths(1));

        long totalFields = fieldRepository.count();
        long totalBookings = bookingRepository.count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("newUsers", newUsersThisMonth);
        model.addAttribute("totalFields", totalFields);
        model.addAttribute("totalBookings", totalBookings);

        // Revenue this month
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        java.time.LocalDateTime endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(23,59,59);
        java.math.BigDecimal revenue = paymentRepository.sumAmountBetween(startOfMonth, endOfMonth);
        model.addAttribute("revenue", revenue != null ? revenue : java.math.BigDecimal.ZERO);

        // Average rating
        Double avg = reviewRepository.findGlobalAverageRating();
        model.addAttribute("averageRating", avg != null ? String.format("%.1f", avg) : "0.0");

        // Pending bookings
        long pending = bookingRepository.countByPendingStatus();
        model.addAttribute("pendingBookings", pending);

        // Recent activities: combine recent users, bookings, reviews, payments
        java.util.List<java.lang.Object[]> activities = new java.util.ArrayList<>();
        // recent users
        java.util.List<com.Fisport.model.User> recentUsers = userRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    recentUsers.stream().limit(5).filter(u -> u.getCreatedAt() != null).forEach(u -> activities.add(new java.lang.Object[]{"USER_REGISTRATION", u.getUsername(), u.getCreatedAt()}));
        // recent bookings
        java.util.List<com.Fisport.model.Booking> recentBookings = bookingRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    recentBookings.stream().limit(5).filter(b -> b.getCreatedAt() != null).forEach(b -> activities.add(new java.lang.Object[]{"BOOKING", b.getUser() != null ? b.getUser().getUsername() : "", b.getCreatedAt()}));
        // recent reviews
        java.util.List<com.Fisport.model.Review> recentReviews = reviewRepository.findTop5ByOrderByCreatedAtDesc();
    recentReviews.stream().limit(5).filter(r -> r.getCreatedAt() != null).forEach(r -> activities.add(new java.lang.Object[]{"REVIEW", r.getUser() != null ? r.getUser().getUsername() : "", r.getCreatedAt()}));
        // recent payments
        java.util.List<com.Fisport.model.Payment> recentPayments = paymentRepository.findTop5ByOrderByCreateAtDesc();
    recentPayments.stream().limit(5).filter(p -> p.getCreateAt() != null).forEach(p -> activities.add(new java.lang.Object[]{"PAYMENT", p.getBooking() != null && p.getBooking().getUser() != null ? p.getBooking().getUser().getUsername() : "", p.getCreateAt()}));

        // sort activities by timestamp desc (nulls last) and limit to 8
        activities.sort((a, b) -> {
            java.time.LocalDateTime ta = (java.time.LocalDateTime) a[2];
            java.time.LocalDateTime tb = (java.time.LocalDateTime) b[2];
            if (ta == null && tb == null) return 0;
            if (ta == null) return 1; // null -> put after
            if (tb == null) return -1;
            return tb.compareTo(ta); // desc
        });
        java.util.List<java.lang.Object[]> limited = activities.stream().limit(8).toList();
        model.addAttribute("recentActivities", limited);

        // booking labels and data for last 7 days
        java.time.LocalDate to = java.time.LocalDate.now();
        java.time.LocalDate from = to.minusDays(6);
        java.util.List<java.lang.Object[]> counts = bookingRepository.countByDateRange(from, to);
        java.util.Map<java.time.LocalDate, Long> map = new java.util.HashMap<>();
        counts.forEach(arr -> {
            if (arr != null && arr.length >= 2) {
                java.time.LocalDate d = (java.time.LocalDate) arr[0];
                Long c = ((Number) arr[1]).longValue();
                map.put(d, c);
            }
        });
        java.util.List<String> labels = new java.util.ArrayList<>();
        java.util.List<Long> data = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            java.time.LocalDate d = from.plusDays(i);
            labels.add(d.getDayOfMonth() + "/" + d.getMonthValue());
            data.add(map.getOrDefault(d, 0L));
        }
        model.addAttribute("bookingLabels", labels);
        model.addAttribute("bookingData", data);

        return "admin/home";
    }
}
