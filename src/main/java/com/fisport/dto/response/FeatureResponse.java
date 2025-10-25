package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FeatureResponse {
    private Long id;
    private String name;
    private String slug;
}
