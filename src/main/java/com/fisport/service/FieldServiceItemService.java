package com.fisport.service;

import com.fisport.dto.response.FieldServiceItemResponse;

import java.util.List;

public interface FieldServiceItemService {
    List<FieldServiceItemResponse> getAllByActive(Long fieldId);
}
