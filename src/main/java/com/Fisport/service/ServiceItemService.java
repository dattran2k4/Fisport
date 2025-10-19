package com.Fisport.service;

import com.Fisport.dto.response.ServiceItemResponse;

import java.util.List;

public interface ServiceItemService {
    List<ServiceItemResponse> findAll();
}
