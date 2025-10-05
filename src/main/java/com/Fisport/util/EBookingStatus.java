package com.Fisport.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EBookingStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("confirmed")
    CONFIRMED;
}
