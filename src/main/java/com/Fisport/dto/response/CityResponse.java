package com.Fisport.dto.response;

import java.util.HashSet;
import java.util.Set;

public class CityResponse {

    private String name;

    private String slug;

    private Set<WardResponse> wardResponses = new HashSet<>();
}
