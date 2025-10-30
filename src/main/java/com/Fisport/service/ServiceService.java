package com.Fisport.service;

import com.Fisport.dto.request.ServiceRequest;
import com.Fisport.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();
    List<ServiceResponse> searchServices(String keyword);
    ServiceResponse getServiceById(Long id);
    ServiceResponse createService(ServiceRequest request);
    ServiceResponse updateService(Long id, ServiceRequest request);
    void deleteService(Long id);
}

