package com.fisport.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ELevel {
    @JsonProperty("yeu")
    YEU("Yếu" ,800, 999),
    @JsonProperty("trungbinh_yeu")
    TRUNGBINH_YEU("Trung bình yếu", 1000, 1199),
    @JsonProperty("trungbinh")
    TRUNGBINH("Trung bình", 1200, 1399),
    @JsonProperty("trungbinh_kha")
    TRUNGBINH_KHA("Trung bình khá", 1400, 1599),
    @JsonProperty("kha")
    KHA("Khá", 1600, 1799),
    @JsonProperty("chuyen_nghiep")
    CHUYEN_NGHIEP("Chuyên nghiệp", 1800, Integer.MAX_VALUE);

    private final String value;
    private final int minElo;
    private final int maxElo;

    public static ELevel fromElo(int elo) {
        for (ELevel level : values()) {
            if (elo >= level.minElo && elo <= level.maxElo) {
                return level;
            }
        }
        return YEU; // default
    }
}
