package com.fisport.service;

import com.fisport.dto.response.FieldHasFeatureResponse;

import java.util.List;

public interface FieldHasFeatureService {
    List<FieldHasFeatureResponse> findFieldHasFeatureByFieldIdByFieldId(Long fieldId);
}
