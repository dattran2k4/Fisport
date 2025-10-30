package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceItemResponse {
    private Long id;
    private String name;
    private Long service_id;
    private String serviceName;
}
