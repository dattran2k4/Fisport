package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class SlotAvailableResponse {

    private LocalTime startTime;
    private boolean isAvailable;
}
