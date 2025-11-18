package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EChallengeStatus {
    OPEN("Chưa có người tham gia"),
    PENDING("Chưa đủ người"),
    FULL("Đầy"),
    MATCHED("Đã hoàn tất"),
    CANCELLED("Huỷ");

    private final String value;

    public String getName() {
        return this.name();
    }
}
