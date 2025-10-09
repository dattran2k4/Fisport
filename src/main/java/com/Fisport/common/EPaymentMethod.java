package com.Fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EPaymentMethod {
    @JsonProperty("momo")
    MOMO,
    @JsonProperty("vnpay")
    VNPAY,
    @JsonProperty("bank_transfer")
    BANK_TRANSFER,
}
