package com.fisport.dto.response;


import com.fisport.common.EFieldStatus;
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
    private String slug;
    private LocalTime openTime;
    private LocalTime closeTime;
    private EFieldStatus status;
    private Double rating;
    private WardResponse wardResponse;
    private FieldTypeResponse fieldTypeResponse;
}
