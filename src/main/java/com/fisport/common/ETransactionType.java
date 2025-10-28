package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ETransactionType {
    @JsonProperty("topup")
    TOUP,
    @JsonProperty("payment")
    PAYMENT,
    @JsonProperty("refund")
    REFUND,
    @JsonProperty("received")
    RECEIVED
}
