package com.fisport.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MatchResultRequest {
    @NotNull
    @Min(value = 0, message = "Điểm số phải >= 0")
    private Integer scortTeamA;

    @NotNull
    @Min(value = 0, message = "Điểm số phải >= 0")
    private Integer scortTeamB;
}
