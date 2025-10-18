package com.Fisport.service;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.FieldDetailResponse;
import com.Fisport.dto.response.FieldHasTimeSlotResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.common.EFieldStatus;

import java.util.List;
import java.util.Set;

public interface FieldService {
    List<FieldResponse> getAllFields(Long wardId, Long fieldTypeId, EFieldStatus status, String keyword, Long[] features);

    void createFieldByOwnerId(FieldRequest fieldRequest, String name);

    void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId);

    void changeStatusFieldByAdmin(Long fieldId, EFieldStatus fieldStatus);

    FieldResponse getField(Long fieldId);

    Set<FeatureResponse> getFeautresByField(Long id);

    List<FieldResponse> getAllPendingFields();

    List<FieldResponse> getAllPendingFieldsByOwner(String name);

    FieldDetailResponse findBySlug(String fieldNameSlug);

    List<FieldDetailResponse> getFieldsNearBy(Double lat, Double lng, Double radius);
}
