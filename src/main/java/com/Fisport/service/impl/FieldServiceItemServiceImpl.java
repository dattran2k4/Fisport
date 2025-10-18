package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.repository.FieldServiceItemRepository;
import com.Fisport.service.FieldServiceItemService;
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
