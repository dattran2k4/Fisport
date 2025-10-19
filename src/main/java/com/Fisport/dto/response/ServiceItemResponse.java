package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceItemResponse {
    private String name;
    private Long id;
    private Long service_id;
}
