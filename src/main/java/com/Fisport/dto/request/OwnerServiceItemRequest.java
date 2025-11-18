package com.fisport.dto.request;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO cho request tạo/cập nhật ServiceItem của Owner
 * Bao gồm thông tin để liên kết với Field của Owner
 */
@Data
public class OwnerServiceItemRequest {
    private String name;
    private Long serviceId;
    private Long fieldId; // Field của Owner để liên kết
    private BigDecimal price; // Giá bán tại sân
    private Integer quantity; // Số lượng tồn kho
    private String description; // Mô tả dịch vụ
}

