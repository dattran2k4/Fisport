package com.Fisport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummaryDto {
    private Long totalFields;
    private Long totalBookings;
    private BigDecimal totalRevenue;
    private Long totalUsers;
    private Double avgRating;
    private Long totalReviews;
    private Double bookingGrowth;
    private Double revenueGrowth;
    private BigDecimal avgBookingValue;
    private Long completedBookings;
    private Long canceledBookings;
    private Double completionRate;
}