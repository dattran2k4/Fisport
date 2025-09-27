package com.Fisport.service.impl;

import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.dto.response.WardResponse;
import com.Fisport.model.Field;
import com.Fisport.repository.FieldRepository;
import com.Fisport.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;

    @Override
    public List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId) {
        List<Field> fields = fieldRepository.findByWardIdAndFieldTypeId(wardId, fieldTypeId);
        return fields.stream()
                .map(f -> new FieldResponse(
                        f.getName(),
                        f.getAddress(),
                        f.getBanner(),
                        f.getSlug(),
                        new WardResponse(
                                f.getWard().getId(),
                                f.getWard().getName(),
                                f.getWard().getSlug()
                        ),
                        new FieldTypeResponse(
                                f.getFieldType().getId(),
                                f.getFieldType().getName(),
                                f.getFieldType().getSlug()
                        )
                ))
                .toList();
    }
}
