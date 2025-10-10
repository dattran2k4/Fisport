package com.Fisport.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
public class BookingServiceItemResponse {
    private Long id;
    private String item;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
