package com.fisport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WardResponse {

    private Long id;

    private String name;

    private String slug;

    private CityResponse cityResponse;
}
