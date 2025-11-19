package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ETransactionType {
    @JsonProperty("topup")
    TOUP("Nạp tiền"),
    @JsonProperty("payment")
    PAYMENT("Thanh toán"),
    @JsonProperty("refund")
    REFUND("Hoàn tiền"),
    @JsonProperty("received")
    RECEIVED("Nhận tiền"),
    @JsonProperty("bonus")
    BONUS("Thưởng bonus");

    private final String value;
}
