package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.model.FieldServiceItem;
import com.Fisport.repository.FieldServiceItemRepository;
import com.Fisport.service.FieldServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FieldServiceItemServiceImpl implements FieldServiceItemService {
    private final FieldServiceItemRepository fieldServiceItemRepository;


    @Override
    public List<FieldServiceItemResponse> getAllByActive(Long fieldId) {
        return fieldServiceItemRepository.findByActive(fieldId);
    }

    @Override
    public List<FieldServiceItemResponse> getAllByField(Long fieldId) {
        List<FieldServiceItem> list = fieldServiceItemRepository.findByFieldId(fieldId);
        return list.stream().map(this::toDto).toList();
    }

    public FieldServiceItemResponse toDto(FieldServiceItem fsi) {
        return FieldServiceItemResponse.builder()
                .serviceItemId(fsi.getServiceItem().getId())
                .id(fsi.getId())
                .quantity(fsi.getQuantity())
                .price(fsi.getPrice())
                .serviceItemName(fsi.getServiceItem().getName())
                .serviceName(fsi.getServiceItem().getService().getName())
                .build();
    }
}
