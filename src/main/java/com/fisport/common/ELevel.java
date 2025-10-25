package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ELevel {
    @JsonProperty("Yếu")
    YEU("Yếu"),
    @JsonProperty("Trung bình yếu")
    TRUNGBINH_YEU("Trung bình yếu"),
    @JsonProperty("Trung bình")
    TRUNGBINH("Trung bình"),
    @JsonProperty("Trung bình khá")
    TRUNGBINH_KHA("Trung bình khá"),
    @JsonProperty("Khá")
    KHA("Khá");

    private final String displayName;

    ELevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
