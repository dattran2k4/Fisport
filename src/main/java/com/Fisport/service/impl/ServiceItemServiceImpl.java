package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.ServiceItemResponse;
import com.Fisport.model.Field;
import com.Fisport.model.ServiceItem;
import com.Fisport.repository.ServiceItemRepository;
import com.Fisport.service.ServiceItemService;
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

    private ServiceItemResponse toDto(ServiceItem si) {
        return ServiceItemResponse.builder()
                .id(si.getId())
                .name(si.getName())
                .service_id(si.getService().getId())
                .build();
    }
}
