package com.Fisport.service.impl;

import com.Fisport.common.EBookingStatus;
import com.Fisport.dto.response.*;
import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.FieldRepository;
import com.Fisport.repository.ReviewRepository;
import com.Fisport.repository.UserRepository;
import com.Fisport.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final BookingRepository bookingRepository;
    private final FieldRepository fieldRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReportSummaryDto getEnhancedSummary(Long ownerId, LocalDate from, LocalDate to) {
        // Previous period for comparison
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(from, to);
        LocalDate prevFrom = from.minusDays(daysBetween);
        LocalDate prevTo = from.minusDays(1);

        // Current period stats
        Long totalFields = ownerId == null ? fieldRepository.count() : fieldRepository.countByOwnerId(ownerId);
        Long totalBookings = bookingRepository.countBookings(ownerId, from, to);
        BigDecimal totalRevenue = bookingRepository.sumRevenue(ownerId, from, to, null);
        Long totalUsers = userRepository.countDistinctUsersByBookings(ownerId, from, to);
        Double avgRating = reviewRepository.getAverageRating(ownerId);
        Long totalReviews = reviewRepository.countByOwnerId(ownerId);

        // Previous period stats for comparison
        Long prevBookings = bookingRepository.countBookings(ownerId, prevFrom, prevTo);
        BigDecimal prevRevenue = bookingRepository.sumRevenue(ownerId, prevFrom, prevTo, null);

        // Calculate growth percentages
        Double bookingGrowth = calculateGrowth(prevBookings, totalBookings);
        Double revenueGrowth = calculateGrowth(prevRevenue, totalRevenue);

        // Additional metrics
        BigDecimal avgBookingValue = totalBookings > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(totalBookings), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        Long completedBookings = bookingRepository.countByStatus(ownerId, from, to, EBookingStatus.COMPLETED);
        Long canceledBookings = bookingRepository.countByStatus(ownerId, from, to, EBookingStatus.CANCELLED);
        Double completionRate = totalBookings > 0 ?
                (completedBookings.doubleValue() / totalBookings.doubleValue()) * 100 : 0.0;

        return ReportSummaryDto.builder()
                .totalFields(totalFields)
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .totalUsers(totalUsers)
                .avgRating(avgRating != null ? avgRating : 0.0)
                .totalReviews(totalReviews)
                .bookingGrowth(bookingGrowth)
                .revenueGrowth(revenueGrowth)
                .avgBookingValue(avgBookingValue)
                .completedBookings(completedBookings)
                .canceledBookings(canceledBookings)
                .completionRate(completionRate)
                .build();
    }

    public List<RevenuePointDto> getRevenueSeries(Long ownerId, LocalDate from, LocalDate to) {
        List<Object[]> rows = bookingRepository.revenueGroupByDate(ownerId, from, to);
        return rows.stream()
                .map(r -> {
                    LocalDate date = (LocalDate) r[0];
                    BigDecimal revenue = (BigDecimal) r[1];
                    return new RevenuePointDto(date.toString(), revenue);
                }).collect(Collectors.toList());
    }

    public List<BookingStatusDto> getBookingsByStatus(Long ownerId, LocalDate from, LocalDate to) {
        List<Object[]> rows = bookingRepository.countByStatusGroup(ownerId, from, to);
        return rows.stream()
                .map(r -> new BookingStatusDto((String) r[0], ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<TopFieldDto> getTopFields(Long ownerId, int limit) {
        List<Object[]> rows = bookingRepository.topFieldsByRevenue(ownerId, limit);
        return rows.stream()
                .map(r -> {
                    Long fieldId = ((Number) r[0]).longValue();
                    String name = (String) r[1];
                    Long bookings = ((Number) r[2]).longValue();
                    BigDecimal revenue = (BigDecimal) r[3];
                    Double rating = reviewRepository.getAverageRatingByField(fieldId);
                    return new TopFieldDto(fieldId, name, bookings, revenue, rating);
                }).collect(Collectors.toList());
    }

    public List<HourlyBookingDto> getHourlyBookingDistribution(Long ownerId, LocalDate from, LocalDate to) {
        List<Object[]> rows = bookingRepository.bookingsByHour(ownerId, from, to);
        return rows.stream()
                .map(r -> new HourlyBookingDto(((Number) r[0]).intValue(), ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }

    public ByteArrayInputStream exportComprehensiveExcel(Long ownerId, LocalDate from, LocalDate to) throws IOException {
        ReportSummaryDto summary = getEnhancedSummary(ownerId, from, to);
        List<RevenuePointDto> revenueSeries = getRevenueSeries(ownerId, from, to);
        List<TopFieldDto> topFields = getTopFields(ownerId, 20);
        List<BookingStatusDto> bookingStatus = getBookingsByStatus(ownerId, from, to);
        List<HourlyBookingDto> hourlyDistribution = getHourlyBookingDistribution(ownerId, from, to);

        try (Workbook wb = new XSSFWorkbook()) {
            // Create styles
            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle titleStyle = createTitleStyle(wb);
            CellStyle currencyStyle = createCurrencyStyle(wb);
            CellStyle percentStyle = createPercentStyle(wb);

            // Sheet 1: Summary
            createSummarySheet(wb, summary, from, to, titleStyle, headerStyle, currencyStyle, percentStyle);

            // Sheet 2: Daily Revenue
            createRevenueSheet(wb, revenueSeries, headerStyle, currencyStyle);

            // Sheet 3: Top Fields
            createTopFieldsSheet(wb, topFields, headerStyle, currencyStyle);

            // Sheet 4: Booking Status
            createBookingStatusSheet(wb, bookingStatus, headerStyle);

            // Sheet 5: Hourly Distribution
            createHourlyDistributionSheet(wb, hourlyDistribution, headerStyle);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                wb.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }
    }

    private void createSummarySheet(Workbook wb, ReportSummaryDto summary,
                                    LocalDate from, LocalDate to,
                                    CellStyle titleStyle, CellStyle headerStyle,
                                    CellStyle currencyStyle, CellStyle percentStyle) {
        Sheet sheet = wb.createSheet("Summary");
        int rowIdx = 0;

        // Title
        Row titleRow = sheet.createRow(rowIdx++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Revenue Report - " + from + " to " + to);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        rowIdx++;

        // Summary metrics
        addSummaryRow(sheet, rowIdx++, "Total Fields", summary.getTotalFields(), headerStyle);
        addSummaryRow(sheet, rowIdx++, "Total Bookings", summary.getTotalBookings(), headerStyle);
        addSummaryRow(sheet, rowIdx++, "Total Revenue", summary.getTotalRevenue(), headerStyle, currencyStyle);
        addSummaryRow(sheet, rowIdx++, "Total Users", summary.getTotalUsers(), headerStyle);
        addSummaryRow(sheet, rowIdx++, "Average Rating", summary.getAvgRating(), headerStyle);
        addSummaryRow(sheet, rowIdx++, "Total Reviews", summary.getTotalReviews(), headerStyle);
        rowIdx++;

        addSummaryRow(sheet, rowIdx++, "Booking Growth", summary.getBookingGrowth(), headerStyle, percentStyle);
        addSummaryRow(sheet, rowIdx++, "Revenue Growth", summary.getRevenueGrowth(), headerStyle, percentStyle);
        addSummaryRow(sheet, rowIdx++, "Avg Booking Value", summary.getAvgBookingValue(), headerStyle, currencyStyle);
        addSummaryRow(sheet, rowIdx++, "Completion Rate", summary.getCompletionRate(), headerStyle, percentStyle);
        addSummaryRow(sheet, rowIdx++, "Completed Bookings", summary.getCompletedBookings(), headerStyle);
        addSummaryRow(sheet, rowIdx++, "Canceled Bookings", summary.getCanceledBookings(), headerStyle);

        autoSizeColumns(sheet, 2);
    }

    private void createRevenueSheet(Workbook wb, List<RevenuePointDto> series,
                                    CellStyle headerStyle, CellStyle currencyStyle) {
        Sheet sheet = wb.createSheet("Daily Revenue");
        int rowIdx = 0;

        Row header = sheet.createRow(rowIdx++);
        Cell h1 = header.createCell(0);
        h1.setCellValue("Date");
        h1.setCellStyle(headerStyle);
        Cell h2 = header.createCell(1);
        h2.setCellValue("Revenue");
        h2.setCellStyle(headerStyle);

        for (RevenuePointDto point : series) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(point.label());
            Cell revenueCell = row.createCell(1);
            revenueCell.setCellValue(point.value().doubleValue());
            revenueCell.setCellStyle(currencyStyle);
        }

        autoSizeColumns(sheet, 2);
    }

    private void createTopFieldsSheet(Workbook wb, List<TopFieldDto> topFields,
                                      CellStyle headerStyle, CellStyle currencyStyle) {
        Sheet sheet = wb.createSheet("Top Fields");
        int rowIdx = 0;

        Row header = sheet.createRow(rowIdx++);
        String[] headers = {"Rank", "Field ID", "Field Name", "Bookings", "Revenue", "Rating"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rank = 1;
        for (TopFieldDto field : topFields) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(rank++);
            row.createCell(1).setCellValue(field.fieldId());
            row.createCell(2).setCellValue(field.fieldName());
            row.createCell(3).setCellValue(field.bookings());
            Cell revenueCell = row.createCell(4);
            revenueCell.setCellValue(field.revenue().doubleValue());
            revenueCell.setCellStyle(currencyStyle);
            row.createCell(5).setCellValue(field.rating() != null ? field.rating() : 0.0);
        }

        autoSizeColumns(sheet, 6);
    }

    private void createBookingStatusSheet(Workbook wb, List<BookingStatusDto> statuses, CellStyle headerStyle) {
        Sheet sheet = wb.createSheet("Booking Status");
        int rowIdx = 0;

        Row header = sheet.createRow(rowIdx++);
        Cell h1 = header.createCell(0);
        h1.setCellValue("Status");
        h1.setCellStyle(headerStyle);
        Cell h2 = header.createCell(1);
        h2.setCellValue("Count");
        h2.setCellStyle(headerStyle);

        for (BookingStatusDto status : statuses) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(status.status());
            row.createCell(1).setCellValue(status.count());
        }

        autoSizeColumns(sheet, 2);
    }

    private void createHourlyDistributionSheet(Workbook wb, List<HourlyBookingDto> hourly, CellStyle headerStyle) {
        Sheet sheet = wb.createSheet("Hourly Distribution");
        int rowIdx = 0;

        Row header = sheet.createRow(rowIdx++);
        Cell h1 = header.createCell(0);
        h1.setCellValue("Hour");
        h1.setCellStyle(headerStyle);
        Cell h2 = header.createCell(1);
        h2.setCellValue("Bookings");
        h2.setCellStyle(headerStyle);

        for (HourlyBookingDto hour : hourly) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(hour.hour() + ":00");
            row.createCell(1).setCellValue(hour.count());
        }

        autoSizeColumns(sheet, 2);
    }

    // Helper methods for styling
    private CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private CellStyle createPercentStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
        return style;
    }

    private void addSummaryRow(Sheet sheet, int rowIdx, String label, Object value, CellStyle headerStyle) {
        Row row = sheet.createRow(rowIdx);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(headerStyle);

        Cell valueCell = row.createCell(1);
        if (value instanceof Number) {
            valueCell.setCellValue(((Number) value).doubleValue());
        } else {
            valueCell.setCellValue(value.toString());
        }
    }

    private void addSummaryRow(Sheet sheet, int rowIdx, String label, Object value,
                               CellStyle headerStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowIdx);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(headerStyle);

        Cell valueCell = row.createCell(1);
        if (value instanceof Number) {
            valueCell.setCellValue(((Number) value).doubleValue());
        } else {
            valueCell.setCellValue(value.toString());
        }
        valueCell.setCellStyle(valueStyle);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private Double calculateGrowth(Number previous, Number current) {
        if (previous == null || current == null || previous.doubleValue() == 0) return 0.0;
        double prev = previous.doubleValue();
        double curr = current.doubleValue();
        return ((curr - prev) / prev) * 100;
    }

    private Double calculateGrowth(BigDecimal previous, BigDecimal current) {
        if (previous == null || current == null || previous.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        BigDecimal growth = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return growth.doubleValue();
    }
}