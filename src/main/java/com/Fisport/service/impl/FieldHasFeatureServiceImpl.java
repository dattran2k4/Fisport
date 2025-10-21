package com.Fisport.service.impl;


import com.Fisport.dto.response.FieldHasFeatureResponse;
import com.Fisport.model.FieldHasFeature;
import com.Fisport.repository.FieldHasFeatureRepository;
import com.Fisport.service.FieldHasFeatureService;
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
