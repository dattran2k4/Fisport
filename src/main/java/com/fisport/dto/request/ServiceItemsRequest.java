package com.fisport.dto.request;

import lombok.Data;

@Data
public class ServiceItemsRequest {
    private Long id;
    private Integer quantity;
    private Double price;
}