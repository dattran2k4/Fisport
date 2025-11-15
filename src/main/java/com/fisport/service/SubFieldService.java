package com.fisport.service;

import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.ai.SearchCriteria;
import com.fisport.dto.ai.SubFieldForAIResponse;
import com.fisport.dto.request.SubFieldRequest;
import com.fisport.dto.response.SubFieldResponse;
import com.fisport.model.SubField;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface SubFieldService {
    List<SubFieldResponse> getAllSubFields(Long fieldId, ESubFieldStatus status);

    SubFieldResponse getSubFieldById(Long id);

    void createSubField(SubFieldRequest request, String username) throws AccessDeniedException;

    void updateSubField(Long id, SubFieldRequest request, String username) throws AccessDeniedException;

    void deleteSubField(Long id, String username) throws AccessDeniedException;

    List<SubFieldForAIResponse> findAvailableSubFields(SearchCriteria criteria);

}
