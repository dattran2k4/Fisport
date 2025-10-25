package com.fisport.service;

import com.fisport.dto.request.ServiceItemsRequest;
import com.fisport.dto.response.ServiceItemResponse;

import java.util.List;

public interface ServiceItemService {
    List<ServiceItemResponse> findAll();
    void save(ServiceItemsRequest serviceItemsRequest);
}
