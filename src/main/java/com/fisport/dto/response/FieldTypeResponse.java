package com.fisport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldTypeResponse {
    private Long id;
    private String name;
    private String slug;
}
