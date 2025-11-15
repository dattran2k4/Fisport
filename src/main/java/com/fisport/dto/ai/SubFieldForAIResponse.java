package com.fisport.dto.ai;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class SubFieldForAIResponse {
    private Long id;
    private String name;
    private String address;
    private String fieldName;
    private Long fieldId;
}
