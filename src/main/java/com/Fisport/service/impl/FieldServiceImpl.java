package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.service.FieldService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldServiceImpl implements FieldService {
    @Override
    public List<FieldResponse> getFieldByWardAndType(long wardId, String type) {
        return List.of();
    }
}
