package com.fisport.service;

import com.fisport.dto.response.FieldTypeResponse;

import java.util.List;

public interface FieldTypeService {
    List<FieldTypeResponse> findAll();
    FieldTypeResponse findBySlug(String slug);
}
