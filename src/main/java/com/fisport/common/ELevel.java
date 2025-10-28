package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ELevel {
    @JsonProperty("yeu")
    YEU("Yếu"),
    @JsonProperty("trungbinh_yeu")
    TRUNGBINH_YEU("Trung bình yếu"),
    @JsonProperty("trungbinh")
    TRUNGBINH("Trung bình"),
    @JsonProperty("trungbinh_kha")
    TRUNGBINH_KHA("Trung bình khá"),
    @JsonProperty("kha")
    KHA("Khá");

    private final String displayName;

    ELevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
