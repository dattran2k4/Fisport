package com.Fisport.service;

import com.Fisport.dto.response.DurationResponse;

import java.util.List;

public interface FieldTypeBookDurationService {
    List<DurationResponse> getDurationByFieldTypeId(Long fieldId);
}
