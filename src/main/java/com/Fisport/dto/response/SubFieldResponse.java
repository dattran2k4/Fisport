package com.Fisport.dto.response;

import com.Fisport.common.ESubFieldStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubFieldResponse {
    private Long id;
    private String name;
    private ESubFieldStatus status;
    private LocalDateTime created_at;
    private LocalDateTime update_at;
    private Long fieldId;
}