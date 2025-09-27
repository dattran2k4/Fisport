package com.Fisport.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EFieldStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE;
}
