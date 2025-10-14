package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VoucherResponse {
    private String code;
    private String description;
    private Integer discount;
    private Integer limit;
    private LocalDate startDate;
    private LocalDate endDate;
}
