package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EFieldStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("pending")
    PENDING
}
