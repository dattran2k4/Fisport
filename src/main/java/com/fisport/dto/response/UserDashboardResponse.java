package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDashboardResponse {
    private BigDecimal totalSpent;
    private int pendingCount;
    private int completedCount;
    private int voucherCount;
    private int reviewCount;
    private List<Long> monthlyBookings;
}
