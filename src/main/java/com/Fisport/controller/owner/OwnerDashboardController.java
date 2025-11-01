package com.Fisport.controller.owner;

import com.Fisport.dto.response.*;
import com.Fisport.model.User;
import com.Fisport.repository.UserRepository;
import com.Fisport.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerDashboardController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    @GetMapping({"", "/", "/dashboard", "/reports"})
    public String dashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request,
            Model model,
            Principal principal) {

        model.addAttribute("content", "dashboard");
        model.addAttribute("currentUri", request.getRequestURI());
        Optional<User> user = userRepository.findByUsername(principal.getName());
        Long ownerId = user != null ? user.get().getId() : null;

        if (from == null) from = LocalDate.now().minusMonths(1);
        if (to == null) to = LocalDate.now();

        // Get all data
        ReportSummaryDto summary = reportService.getEnhancedSummary(ownerId, from, to);
        List<RevenuePointDto> series = reportService.getRevenueSeries(ownerId, from, to);
        List<TopFieldDto> topFields = reportService.getTopFields(ownerId, 10);
        List<BookingStatusDto> bookingStatus = reportService.getBookingsByStatus(ownerId, from, to);
        List<HourlyBookingDto> hourlyDistribution = reportService.getHourlyBookingDistribution(ownerId, from, to);

        // LABLE FOR CHART

        List<String> revenueLabels = series.stream()
                .map(RevenuePointDto::label)
                .collect(Collectors.toList());

        List<BigDecimal> revenueData = series.stream()
                .map(RevenuePointDto::value)
                .collect(Collectors.toList());

        model.addAttribute("revenueLabels", revenueLabels);
        model.addAttribute("revenueData", revenueData);
//-------------------
        List<String> bookingLable = bookingStatus.stream()
                .map(BookingStatusDto::status)
                .collect(Collectors.toList());

        List<Long> bookingData = bookingStatus.stream()
                .map(BookingStatusDto::count)
                .toList();

        model.addAttribute("statusLabels", bookingLable);
        model.addAttribute("statusData", bookingData);
//-------------------
        List<Integer> hourlyLabels = hourlyDistribution.stream()
                .map(HourlyBookingDto::hour)
                .collect(Collectors.toList());

        List<Long> hourlyData = hourlyDistribution.stream()
                .map(HourlyBookingDto::count)
                .toList();

        model.addAttribute("hourlyLabels", hourlyLabels);
        model.addAttribute("hourlyData", hourlyData);
        // Add to model
        model.addAttribute("summary", summary);
        model.addAttribute("topFields", topFields);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        // Format dates for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        model.addAttribute("formattedFrom", from.format(formatter));
        model.addAttribute("formattedTo", to.format(formatter));

        return "owner/dashboard";
    }

    @GetMapping("/reports/export")
    public void exportExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletResponse response,
            @AuthenticationPrincipal User user) throws IOException {

        Long ownerId = user != null ? user.getId() : null;

        if (from == null) from = LocalDate.now().minusMonths(1);
        if (to == null) to = LocalDate.now();

        ByteArrayInputStream excelStream = reportService.exportComprehensiveExcel(ownerId, from, to);

        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = String.format("fisport-report-%s-to-%s.xlsx", from, to);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Copy stream to response
        IOUtils.copy(excelStream, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("/reports/quick-stats")
    public String quickStats(
            Model model,
            @AuthenticationPrincipal User user) {

        Long ownerId = user != null ? user.getId() : null;
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);

        // Get quick stats for last 7 days
        ReportSummaryDto weeklyStats = reportService.getEnhancedSummary(ownerId, weekAgo, today);

        model.addAttribute("weeklyStats", weeklyStats);
        return "owner/quick-stats";
    }
}