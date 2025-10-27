package com.Fisport.dto.response;

import com.Fisport.common.EFieldServiceItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OwnerFieldServiceResponse {
    private Long fieldServiceItemId;
    private Long fieldId;
    private String fieldName;
    private Long serviceItemId;
    private String serviceItemName;
    private String serviceName;
    private BigDecimal price;
    private Integer quantity;
    private EFieldServiceItem status;
}




