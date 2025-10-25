package com.fisport.dto.response;

import com.fisport.common.EVoucherStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private String description;
    private Integer discount;
    private Integer limit;
    private LocalDate startDate;
    private LocalDate endDate;
    private EVoucherStatus status;
    private boolean isActive;
}
