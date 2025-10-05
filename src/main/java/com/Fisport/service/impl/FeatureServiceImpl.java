package com.Fisport.service.impl;

import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.model.Feature;
import com.Fisport.repository.FeatureRepository;
import com.Fisport.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;


    @Override
    public List<FeatureResponse> getListFeatures() {
        List<Feature> features = featureRepository.findAll();
        return (features.stream().map(feature -> FeatureResponse.builder()
                .id(feature.getId())
                .name(feature.getName())
                .slug(feature.getSlug())
                .build() ).toList());
    }
}
