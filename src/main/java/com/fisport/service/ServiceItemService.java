package com.fisport.service;

import com.fisport.dto.request.ServiceItemRequest;
import com.fisport.dto.response.ServiceItemResponse;


import java.util.List;

public interface ServiceItemService {
    List<ServiceItemResponse> findAll();
    List<ServiceItemResponse> findAllByServiceId(Long serviceId);
    ServiceItemResponse findById(Long id);
    ServiceItemResponse createServiceItem(ServiceItemRequest request);
    ServiceItemResponse updateServiceItem(Long id, ServiceItemRequest request);
    void deleteServiceItem(Long id);
}
