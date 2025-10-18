package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ESubFieldStatus {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("maintain")
    MAINTAIN,
    @JsonProperty("inactive")
    INACTIVE,;
}
