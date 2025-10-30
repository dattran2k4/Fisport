package com.fisport.service.impl;


import com.fisport.dto.response.FieldHasFeatureResponse;
import com.fisport.model.FieldHasFeature;
import com.fisport.repository.FieldHasFeatureRepository;
import com.fisport.service.FieldHasFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldHasFeatureServiceImpl implements FieldHasFeatureService {

    @Autowired
    private FieldHasFeatureRepository fieldHasFeatureRepository;
    @Override
    public List<FieldHasFeatureResponse> findFieldHasFeatureByFieldIdByFieldId(Long fieldId) {
        List<FieldHasFeature> list = fieldHasFeatureRepository.findFieldHasFeatureByFieldId(fieldId);
        return list.stream().map(this::toDto).toList();
    }

    public FieldHasFeatureResponse toDto(FieldHasFeature feature) {
        return FieldHasFeatureResponse.builder()
                .field_id(feature.getField().getId())
                .id(feature.getId())
                .feature_id(feature.getFeature().getId())
                .build();
    }
}
