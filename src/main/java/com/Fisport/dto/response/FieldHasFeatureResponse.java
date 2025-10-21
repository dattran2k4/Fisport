package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FieldHasFeatureResponse {
    private Long field_id;
    private Long id;
    private Long feature_id;
}
