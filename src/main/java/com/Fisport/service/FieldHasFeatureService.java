package com.Fisport.service;

import com.Fisport.dto.response.FieldHasFeatureResponse;
import com.Fisport.model.FieldHasFeature;

import java.util.List;
import java.util.Set;

public interface FieldHasFeatureService {
    List<FieldHasFeatureResponse> findFieldHasFeatureByFieldIdByFieldId(Long fieldId);
}
