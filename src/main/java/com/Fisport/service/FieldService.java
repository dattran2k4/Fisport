package com.Fisport.service;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.FieldResponse;

import java.util.List;

public interface FieldService {
    List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId);
    List<FieldResponse> getFieldByFieldTypeId(long fieldTypeId);
    List<FieldResponse> getFieldByOwnerId(Long ownerId);
    void createFieldByOwnerId(FieldRequest fieldRequest, Long ownerId);
    void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId);
}
