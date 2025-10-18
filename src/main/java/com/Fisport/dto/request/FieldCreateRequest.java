package com.Fisport.dto.request;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class FieldCreateRequest {
    private String name;
    private String address;
    private Long city;
    private String description;
    private String field_type;
    private String longitude;
    private String latitude;
    private String banner;
    private String ward;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<String> features;
}