package com.fisport.service.impl;

import com.fisport.dto.request.ServiceItemRequest;
import com.fisport.dto.response.ServiceItemResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Service;
import com.fisport.model.ServiceItem;
import com.fisport.repository.ServiceItemRepository;
import com.fisport.repository.ServiceRepository;
import com.fisport.service.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class    ServiceItemServiceImpl implements ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemResponse> findAll() {
        List<ServiceItem> serviceItems = serviceItemRepository.findAll();
        return serviceItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemResponse> findAllByServiceId(Long serviceId) {
        // Kiểm tra service có tồn tại không
        if (!serviceRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Không tìm thấy Service với ID: " + serviceId);
        }
        
        List<ServiceItem> serviceItems = serviceItemRepository.findByServiceId(serviceId);
        return serviceItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceItemResponse findById(Long id) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceItem với ID: " + id));
        return toDto(serviceItem);
    }

    @Override
    @Transactional
    public ServiceItemResponse createServiceItem(ServiceItemRequest request) {
        // Kiểm tra Service có tồn tại không
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Service với ID: " + request.getServiceId()));
        
        // Kiểm tra tên sản phẩm đã tồn tại trong service này chưa
        serviceItemRepository.findByNameAndServiceId(request.getName(), request.getServiceId())
                .ifPresent(si -> {
                    throw new InvalidDataException("Sản phẩm '" + request.getName() + "' đã tồn tại trong loại dịch vụ này");
                });

        ServiceItem serviceItem = ServiceItem.builder()
                .name(request.getName())
                .service(service)
                .build();

        ServiceItem savedServiceItem = serviceItemRepository.save(serviceItem);
        return toDto(savedServiceItem);
    }

    @Override
    @Transactional
    public ServiceItemResponse updateServiceItem(Long id, ServiceItemRequest request) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceItem với ID: " + id));

        // Kiểm tra Service có tồn tại không
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Service với ID: " + request.getServiceId()));

        // Kiểm tra tên mới có trùng với sản phẩm khác trong cùng service không
        serviceItemRepository.findByNameAndServiceId(request.getName(), request.getServiceId())
                .ifPresent(si -> {
                    if (!si.getId().equals(id)) {
                        throw new InvalidDataException("Sản phẩm '" + request.getName() + "' đã tồn tại trong loại dịch vụ này");
                    }
                });

        serviceItem.setName(request.getName());
        serviceItem.setService(service);
        
        ServiceItem updatedServiceItem = serviceItemRepository.save(serviceItem);
        return toDto(updatedServiceItem);
    }

    @Override
    @Transactional
    public void deleteServiceItem(Long id) {
        ServiceItem serviceItem = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceItem với ID: " + id));
        serviceItemRepository.delete(serviceItem);
    }

    private ServiceItemResponse toDto(ServiceItem si) {
        return ServiceItemResponse.builder()
                .id(si.getId())
                .name(si.getName())
                .service_id(si.getService().getId())
                .serviceName(si.getService().getName())
                .build();
    }
}
