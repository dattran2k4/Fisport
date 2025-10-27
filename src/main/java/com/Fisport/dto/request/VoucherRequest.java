package com.Fisport.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class VoucherRequest {
    private String code;
    private String description;
    private Integer discount; // Percentage (%)
    private Integer limit; // Quantity limit
    private LocalDate startDate;
    private LocalDate endDate;
}

