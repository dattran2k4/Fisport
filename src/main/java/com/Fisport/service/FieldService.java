package com.Fisport.service;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.util.EFieldStatus;

import java.util.List;

public interface FieldService {
    List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId);
    List<FieldResponse> getFieldByFieldTypeId(long fieldTypeId);
    List<FieldResponse> getFieldByOwnerId(Long ownerId);
    void createFieldByOwnerId(FieldRequest fieldRequest, Long ownerId);

    void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId);
    void changeStatusFieldByOwnerId(Long ownerId, Long fieldId,  EFieldStatus fieldStatus);
    FieldResponse getField(Long fieldId);
}
