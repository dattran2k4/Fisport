package com.Fisport.service.impl;

import com.Fisport.dto.response.DurationResponse;
import com.Fisport.model.Duration;
import com.Fisport.model.FieldType;
import com.Fisport.repository.FieldTypeBookDurationRepository;
import com.Fisport.repository.FieldTypeRepository;
import com.Fisport.service.FieldTypeBookDurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FieldBookDurationServiceImpl implements FieldTypeBookDurationService {

    private final FieldTypeBookDurationRepository  fieldTypeBookDurationRepository;

    @Override
    public List<DurationResponse> getDurationByFieldTypeId(Long id) {
        List<Duration> durations = fieldTypeBookDurationRepository.findDurationByFieldTypeId(id);
        return (durations.stream().map(f -> DurationResponse.builder()
                .id(f.getId())
                .minutes(f.getMinutes())
                .build())).toList();
    }
}
