package com.fisport.service;

import com.fisport.dto.response.DurationResponse;

import java.util.List;

public interface FieldTypeBookDurationService {
    List<DurationResponse> getDurationByFieldTypeId(Long fieldId);
}
