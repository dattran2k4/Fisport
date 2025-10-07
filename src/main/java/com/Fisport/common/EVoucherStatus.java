package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EVoucherStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE
}
