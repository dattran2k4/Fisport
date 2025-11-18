package com.fisport.dto.request;

import lombok.Data;

/**
 * DTO cho request tạo/cập nhật Service của Owner
 * Owner chỉ có thể quản lý services cho sân của mình
 */
@Data
public class OwnerServiceRequest {
    private String name;
}

