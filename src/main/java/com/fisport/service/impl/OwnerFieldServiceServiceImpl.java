package com.fisport.service.impl;

import com.fisport.dto.response.OwnerFieldServiceResponse;
import com.fisport.model.FieldServiceItem;
import com.fisport.repository.FieldServiceItemRepository;
import com.fisport.service.OwnerFieldServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerFieldServiceServiceImpl implements OwnerFieldServiceService {

    private final FieldServiceItemRepository fieldServiceItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OwnerFieldServiceResponse> getAllFieldServicesByOwner(Long ownerId) {
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        
        return fieldServiceItems.stream()
                .map(this::toOwnerFieldServiceResponse)
                .collect(Collectors.toList());
    }

    private OwnerFieldServiceResponse toOwnerFieldServiceResponse(FieldServiceItem fsi) {
        return OwnerFieldServiceResponse.builder()
                .fieldServiceItemId(fsi.getId())
                .fieldId(fsi.getField().getId())
                .fieldName(fsi.getField().getName())
                .serviceItemId(fsi.getServiceItem().getId())
                .serviceItemName(fsi.getServiceItem().getName())
                .serviceName(fsi.getServiceItem().getService().getName())
                .price(fsi.getPrice())
                .quantity(fsi.getQuantity())
                .status(fsi.getStatus())
                .build();
    }
}




