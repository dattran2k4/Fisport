package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EPaymentStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("success")
    SUCCESS,
    @JsonProperty("failed")
    FAILED,
    @JsonProperty("refunded")
    REFUNDED,
}
