package com.Fisport.service;

import com.Fisport.dto.response.FieldTypeResponse;

import java.util.List;
import java.util.Set;

public interface FieldTypeService {
    List<FieldTypeResponse> findAll();
}
