package com.Fisport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FieldTypeResponse {
    private Long id;
    private String name;
    private String slug;
}
