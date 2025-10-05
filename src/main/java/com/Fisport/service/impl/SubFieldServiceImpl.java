package com.Fisport.service.impl;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.SubFieldRequest;
import com.Fisport.dto.response.SubFieldResponse;
import com.Fisport.model.SubField;
import com.Fisport.repository.SubFieldRepository;
import com.Fisport.service.SubFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubFieldServiceImpl implements SubFieldService {
    private final SubFieldRepository  subFieldRepository;

    @Override
    public List<SubFieldResponse> getAllSubFields(Long fieldId, ESubFieldStatus status) {
        List<SubField> list = subFieldRepository.findAllSubFields(fieldId, status);
        return list.stream().map(this::toDto).toList();
    }

    @Override
    public SubFieldResponse getSubFieldById(Long id) {
        return null;
    }

    @Override
    public void createSubField(SubFieldRequest request) {

    }

    @Override
    public void updateSubField(Long id, SubFieldRequest request) {

    }

    @Override
    public void deleteSubField(Long id) {

    }

    private SubFieldResponse toDto(SubField subField) {
        return SubFieldResponse.builder()
                .id(subField.getId())
                .name(subField.getName())
                .status(subField.getStatus())
                .fieldId(subField.getField().getId())
                .build();
    }
}
