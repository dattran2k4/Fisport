package com.Fisport.service;

import com.Fisport.dto.request.ServiceItemRequest;
import com.Fisport.dto.response.ServiceItemResponse;

import java.util.List;

public interface ServiceItemService {
    List<ServiceItemResponse> findAll();
    List<ServiceItemResponse> findAllByServiceId(Long serviceId);
    ServiceItemResponse findById(Long id);
    ServiceItemResponse createServiceItem(ServiceItemRequest request);
    ServiceItemResponse updateServiceItem(Long id, ServiceItemRequest request);
    void deleteServiceItem(Long id);
}
