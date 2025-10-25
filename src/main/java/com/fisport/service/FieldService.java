package com.fisport.service;

import com.fisport.dto.request.FieldCreateRequest;
import com.fisport.dto.request.FieldRequest;
import com.fisport.dto.response.FeatureResponse;
import com.fisport.dto.response.FieldDetailResponse;
import com.fisport.dto.response.FieldResponse;
import com.fisport.common.EFieldStatus;

import java.util.List;
import java.util.Set;

public interface FieldService {
    List<FieldResponse> getAllFields(Long wardId, Long fieldTypeId, EFieldStatus status, String keyword, Long[] features);

    void createFieldByOwnerId(FieldRequest fieldRequest, String name);

    void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId);
    public void createFieldByOwner(FieldCreateRequest fieldCreateRequest, String name);
    void changeStatusFieldByAdmin(Long fieldId, EFieldStatus fieldStatus);

    FieldResponse getField(Long fieldId);
    List<FieldResponse> getMyOwnerFields(String name);
    Set<FeatureResponse> getFeautresByField(Long id);

    List<FieldResponse> getAllPendingFields();

    List<FieldResponse> getAllPendingFieldsByOwner(String name);

    FieldDetailResponse findBySlug(String fieldNameSlug);

    List<FieldDetailResponse> getFieldsNearBy(Double lat, Double lng, Double radius);
}
