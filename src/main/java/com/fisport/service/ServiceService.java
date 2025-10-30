package com.fisport.service;

import com.fisport.dto.request.ServiceRequest;
import com.fisport.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();
    List<ServiceResponse> searchServices(String keyword);
    ServiceResponse getServiceById(Long id);
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse updateService(Long id, ServiceRequest request);
    void deleteService(Long id);
}

