package com.Fisport.dto.response;

import com.Fisport.model.City;
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
