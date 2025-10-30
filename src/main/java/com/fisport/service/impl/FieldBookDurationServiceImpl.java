package com.fisport.service.impl;

import com.fisport.dto.response.DurationResponse;
import com.fisport.model.Duration;
import com.fisport.repository.FieldTypeBookDurationRepository;
import com.fisport.service.FieldTypeBookDurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FieldBookDurationServiceImpl implements FieldTypeBookDurationService {

    private final FieldTypeBookDurationRepository fieldTypeBookDurationRepository;

    @Override
    public List<DurationResponse> getDurationByFieldTypeId(Long id) {
        List<Duration> durations = fieldTypeBookDurationRepository.findDurationByFieldTypeId(id);
        return (durations.stream().map(f -> DurationResponse.builder()
                .id(f.getId())
                .minutes(f.getMinutes())
                .build())).toList();
    }
}
