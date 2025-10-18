package com.Fisport.service;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.SubFieldRequest;
import com.Fisport.dto.response.SubFieldResponse;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

public interface SubFieldService {
    List<SubFieldResponse> getAllSubFields(Long fieldId, ESubFieldStatus status);
    SubFieldResponse getSubFieldById(Long id);
    void createSubField(SubFieldRequest request, String username) throws AccessDeniedException;
    void updateSubField(Long id, SubFieldRequest request, String username) throws AccessDeniedException;
    void deleteSubField(Long id, String username) throws AccessDeniedException;
}
