package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EGender {
    @JsonProperty("male")
    MALE("Nam"),
    @JsonProperty("female")
    FEMALE("Nữ"),
    @JsonProperty("other")
    OTHER("Khác");

    private final String value;
}
