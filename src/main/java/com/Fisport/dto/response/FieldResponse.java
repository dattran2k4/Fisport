package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldResponse {
    private String name;
    private String address;
    private String banner;
    private String slug;
}
