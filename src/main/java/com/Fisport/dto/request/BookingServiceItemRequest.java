package com.Fisport.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookingServiceItemRequest {

    @NotNull
    private Long fieldServiceItemId;

    @Min(1)
    private int quantity;
}
