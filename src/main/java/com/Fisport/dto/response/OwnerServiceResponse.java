package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

/**
 * DTO response cho Service của Owner
 * Bao gồm trạng thái enabled/disabled cho field cụ thể
 */
@Builder
@Getter
public class OwnerServiceResponse {
    private Long id;
    private String name;
    private Boolean enabled; // Service có được bật cho field này không
    private List<OwnerServiceItemResponse> serviceItems;
    private Integer totalFields; // Số lượng fields của owner đang sử dụng service này
}

