package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DurationResponse {
    private Long id;
    private int minutes;
}
