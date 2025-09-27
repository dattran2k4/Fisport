package com.Fisport.service;

import com.Fisport.dto.response.FieldResponse;

import java.util.List;

public interface FieldService {
    List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId);
}
