package com.Fisport.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Builder
@Getter
public class FieldHasTimeSlotResponse {
    private Long id;
    private LocalTime startTime;
    private BigDecimal price;
}
