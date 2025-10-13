package com.Fisport.dto.response;

import com.Fisport.common.EFieldStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FieldDetailResponse {
    private Long id;
    private String name;
    private String address;
    private String banner;
    private String slug;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private EFieldStatus status;
    private Integer rating;
    private String review;
    private String ward;
    private String city;
    private String type;
    private List<String> features;
}
