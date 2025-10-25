package com.fisport.dto.request;

import com.fisport.common.ELevel;
import com.fisport.dto.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ChallengeMatchRequest {

    private BigDecimal fee;

    @NotBlank(message = "Nhập tiêu đề cho trận đấu")
    private String title;

    @EnumValue(name = "level", enumClass = ELevel.class)
    private ELevel level;

    @NotNull(message = "Nhập số người chơi tối đa trong 1 trận đấu")
    private int maxPlayers;

    @NotNull(message = "Vui lòng đặt lịch thi đấu để tạo trận thách đấu")
    private BookingRequest bookingRequest;

    @NotBlank(message = "Nhập ghi chú cho trận đấu")
    private String note;
}
