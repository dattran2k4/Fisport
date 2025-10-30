package com.Fisport.service.impl;

import com.Fisport.dto.request.ServiceRequest;
import com.Fisport.dto.response.ServiceItemResponse;
import com.Fisport.dto.response.ServiceResponse;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Service;
import com.Fisport.repository.ServiceRepository;
import com.Fisport.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return services.stream()
                .map(this::toServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> searchServices(String keyword) {
        List<Service> services = (keyword == null || keyword.trim().isEmpty())
                ? serviceRepository.findAll()
                : serviceRepository.findByNameContainingIgnoreCase(keyword.trim());
        return services.stream()
                .map(this::toServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ với ID: " + id));
        return toServiceResponse(service);
    }

    @Override
    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        // Kiểm tra tên dịch vụ đã tồn tại chưa
        serviceRepository.findByName(request.getName()).ifPresent(s -> {
            throw new InvalidDataException("Dịch vụ với tên '" + request.getName() + "' đã tồn tại");
        });

        Service service = Service.builder()
                .name(request.getName())
                .build();

        Service savedService = serviceRepository.save(service);
        return toServiceResponse(savedService);
    }

    @Override
    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ với ID: " + id));

        // Kiểm tra tên mới có trùng với dịch vụ khác không
        serviceRepository.findByName(request.getName()).ifPresent(s -> {
            if (!s.getId().equals(id)) {
                throw new InvalidDataException("Dịch vụ với tên '" + request.getName() + "' đã tồn tại");
            }
        });

        service.setName(request.getName());
        Service updatedService = serviceRepository.save(service);
        return toServiceResponse(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ với ID: " + id));
        serviceRepository.delete(service);
    }

    private ServiceResponse toServiceResponse(Service service) {
        List<ServiceItemResponse> serviceItemResponses;
        try {
            serviceItemResponses = service.getServiceItems() != null && !service.getServiceItems().isEmpty()
                    ? service.getServiceItems().stream()
                    .map(item -> ServiceItemResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .service_id(service.getId())
                            .build())
                    .collect(Collectors.toList())
                    : List.of();
        } catch (Exception e) {
            // Nếu có lỗi khi load serviceItems, trả về list rỗng
            serviceItemResponses = List.of();
        }

        return ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .serviceItems(serviceItemResponses)
                .build();
    }
}

