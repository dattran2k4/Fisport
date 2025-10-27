package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ServiceResponse {
    private Long id;
    private String name;
    private List<ServiceItemResponse> serviceItems;
}

