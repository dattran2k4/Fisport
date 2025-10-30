package com.fisport.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingServiceItemRequest {

//    @NotNull
    private Long fieldServiceItemId;

//    @Min(1)
    private int quantity;
}
