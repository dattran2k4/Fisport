package com.fisport.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EChallengeStatus {
    OPEN("Chưa có người tham gia"),
    PENDING("Chưa đủ người"),
    FULL("Đầy"),
    MATCHED("Đã hoàn tất"),
    CANCELLED("Huỷ");

    private final String value;
}
