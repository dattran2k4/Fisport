package com.Fisport.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class FieldResponse {
    private String name;
    private String address;
    private String banner;
    private String slug;

    private WardResponse wardResponse;
    private FieldTypeResponse fieldTypeResponse;
}
