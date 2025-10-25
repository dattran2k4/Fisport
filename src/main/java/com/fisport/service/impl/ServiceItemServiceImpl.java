package com.fisport.service.impl;

import com.fisport.dto.request.ServiceItemsRequest;
import com.fisport.dto.response.ServiceItemResponse;
import com.fisport.model.ServiceItem;
import com.fisport.repository.ServiceItemRepository;
import com.fisport.service.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceItemServiceImpl implements ServiceItemService {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Override
    public List<ServiceItemResponse> findAll() {
        List<ServiceItem> serviceItems = serviceItemRepository.findAll();
        return serviceItems.stream().map(this::toDto).toList();
    }

    @Override
    public void save(ServiceItemsRequest serviceItemsRequest) {

    }

    private ServiceItemResponse toDto(ServiceItem si) {
        return ServiceItemResponse.builder()
                .id(si.getId())
                .name(si.getName())
                .service_id(si.getService().getId())
                .build();
    }
}
