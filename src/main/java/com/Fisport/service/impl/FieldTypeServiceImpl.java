package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.model.FieldType;
import com.Fisport.repository.FieldTypeRepository;
import com.Fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FieldTypeServiceImpl implements FieldTypeService {

    private final FieldTypeRepository fieldTypeRepository;

    @Override
    public List<FieldTypeResponse> findAll() {
        List<FieldType> fields = fieldTypeRepository.findAll();
        return fields.stream()
                .map(field -> FieldTypeResponse.builder()
                        .id(field.getId())
                        .name(field.getName())
                        .slug(field.getSlug())
                        .build()
                )
                .toList();
    }
}
