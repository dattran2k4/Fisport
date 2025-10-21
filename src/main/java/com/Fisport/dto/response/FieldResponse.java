package com.Fisport.dto.response;


import com.Fisport.common.EFieldStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class FieldResponse {
    private Long id;
    private String name;
    private String address;
    private String banner;
    private String description;
    private String fieldTypeId;
    private String slug;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double latitude;
    private Double longitude;
    private EFieldStatus status;
    private Double rating;
    private WardResponse wardResponse;
    private FieldTypeResponse fieldTypeResponse;
}
