package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.repository.FieldServiceItemRepository;
import com.Fisport.service.FieldServiceItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FieldServiceItemImpl implements FieldServiceItem {
    private final FieldServiceItemRepository fieldServiceItemRepository;


    @Override
    public List<FieldServiceItemResponse> getAllByActive(Long fieldId) {
        List<FieldServiceItem> list = fieldServiceItemRepository.findByActive(fieldId);
        return list.stream().map(f -> FieldServiceItemResponse.builder()
                .
                .build()).toList();
    }
}
