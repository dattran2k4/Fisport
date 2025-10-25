package com.fisport.service.impl;

import com.fisport.dto.response.FieldServiceItemResponse;
import com.fisport.repository.FieldServiceItemRepository;
import com.fisport.service.FieldServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FieldServiceItemServiceImpl implements FieldServiceItemService {
    private final FieldServiceItemRepository fieldServiceItemRepository;


    @Override
    public List<FieldServiceItemResponse> getAllByActive(Long fieldId) {
        List<FieldServiceItemResponse> list = fieldServiceItemRepository.findByActive(fieldId);
        return list;
    }
}
