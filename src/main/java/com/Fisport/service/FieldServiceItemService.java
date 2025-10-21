package com.Fisport.service;

import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.model.FieldServiceItem;

import java.util.List;

public interface FieldServiceItemService {
    List<FieldServiceItemResponse> getAllByActive(Long fieldId);
    List<FieldServiceItemResponse> getAllByField(Long fieldId);
}
