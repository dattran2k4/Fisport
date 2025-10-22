package com.Fisport.common;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum ETransactionStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("SUCCESS")
    SUCCESS,
    @JsonProperty("failed")
    FAILED
}
