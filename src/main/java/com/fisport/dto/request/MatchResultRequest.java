package com.fisport.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultRequest {
    @Min(value = 0, message = "Điểm số phải >= 0")
    private Integer scortTeamA;

    @Min(value = 0, message = "Điểm số phải >= 0")
    private Integer scortTeamB;
}
