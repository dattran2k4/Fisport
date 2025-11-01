package com.Fisport.service;

import com.Fisport.dto.response.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    ReportSummaryDto getEnhancedSummary(Long ownerId, LocalDate from, LocalDate to);

    List<RevenuePointDto> getRevenueSeries(Long ownerId, LocalDate from, LocalDate to);

    List<BookingStatusDto> getBookingsByStatus(Long ownerId, LocalDate from, LocalDate to);

    List<TopFieldDto> getTopFields(Long ownerId, int limit);

    List<HourlyBookingDto> getHourlyBookingDistribution(Long ownerId, LocalDate from, LocalDate to);

    ByteArrayInputStream exportComprehensiveExcel(Long ownerId, LocalDate from, LocalDate to) throws IOException;
}