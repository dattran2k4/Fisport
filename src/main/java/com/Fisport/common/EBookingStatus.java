package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EBookingStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("paid")
    PAID,
    @JsonProperty("failed")
    FAILED,
    @JsonProperty("cancelled")
    CANCELLED,
    @JsonProperty("completed")
    COMPLETED
}
