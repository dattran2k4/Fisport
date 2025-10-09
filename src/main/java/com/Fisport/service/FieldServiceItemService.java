package com.Fisport.service;

import com.Fisport.dto.response.FieldServiceItemResponse;

import java.util.List;

public interface FieldServiceItemService {
    List<FieldServiceItemResponse> getAllByActive(Long fieldId);
}
