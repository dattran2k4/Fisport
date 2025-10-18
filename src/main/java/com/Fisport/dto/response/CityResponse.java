package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class CityResponse {
    private Long id;
    private String name;
    private String slug;
    private Set<WardResponse> wardResponses = new HashSet<>();
}
