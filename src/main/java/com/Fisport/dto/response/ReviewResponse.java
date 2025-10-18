package com.Fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResponse {
    private String fieldBanner;
    private String fieldName;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
}
