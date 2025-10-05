package com.Fisport.service;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.SubFieldRequest;
import com.Fisport.dto.response.SubFieldResponse;

import java.util.List;

public interface SubFieldService {
    List<SubFieldResponse> getAllSubFields(Long fieldId, ESubFieldStatus status);
    SubFieldResponse getSubFieldById(Long id);
    void createSubField(SubFieldRequest request);
    void updateSubField(Long id, SubFieldRequest request);
    void deleteSubField(Long id);
}
