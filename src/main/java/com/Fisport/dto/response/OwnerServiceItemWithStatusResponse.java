package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * DTO cho service item với trạng thái đã áp dụng vào field hay chưa
 */
@Builder
@Getter
public class OwnerServiceItemWithStatusResponse {
    private Long serviceItemId;
    private String serviceItemName;
    private Long serviceId;
    private String serviceName;
    private Boolean isAttached; // true nếu đã được áp dụng vào field
    private Long fieldServiceItemId; // ID của FieldServiceItem nếu đã attached, null nếu chưa
    private BigDecimal price; // Giá tại field này (nếu đã attached)
    private Integer quantity; // Số lượng tại field này (nếu đã attached)
    private String status; // ACTIVE/INACTIVE (nếu đã attached)
}

