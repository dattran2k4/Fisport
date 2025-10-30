package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EUserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("none")
    NONE;
}
