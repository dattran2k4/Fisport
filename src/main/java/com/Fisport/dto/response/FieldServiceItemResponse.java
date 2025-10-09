package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class FieldServiceItemResponse {

    private Long id;
    private Long serviceItemId;
    private int quantity;
    private String serviceItemName;
    private String serviceName;
    private BigDecimal price;
}
