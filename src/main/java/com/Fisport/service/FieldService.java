package com.Fisport.service;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.FieldHasTimeSlotResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.util.EFieldStatus;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface FieldService {
    List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId);
    List<FieldResponse> getFieldByFieldTypeId(long fieldTypeId);
    List<FieldResponse> getFieldByOwnerId(Long ownerId);
    void createFieldByOwnerId(FieldRequest fieldRequest, String name);

    void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId);
    void changeStatusFieldByAdmin(Long fieldId,  EFieldStatus fieldStatus);
    FieldResponse getField(Long fieldId);

    List<FieldHasTimeSlotResponse> getTimeSlotAndPriceByFieldId(Long id);

    Set<FeatureResponse> getFeautresByField(Long id);
}
