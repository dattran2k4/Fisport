package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO response cho ServiceItem của Owner
 * Bao gồm thông tin về các fields đang sử dụng item này
 */
@Builder
@Getter
public class OwnerServiceItemResponse {
    private Long id;
    private String name;
    private Long serviceId;
    private String serviceName;
    // Danh sách các FieldServiceItem (liên kết với fields của owner)
    private List<OwnerFieldServiceItemInfo> fieldServiceItems;
    
    /**
     * Thông tin về FieldServiceItem của owner
     */
    @Builder
    @Getter
    public static class OwnerFieldServiceItemInfo {
        private Long fieldServiceItemId;
        private Long fieldId;
        private String fieldName;
        private BigDecimal price;
        private Integer quantity;
    }
}

