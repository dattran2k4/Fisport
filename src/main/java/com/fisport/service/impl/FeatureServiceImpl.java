package com.fisport.service.impl;

import com.fisport.dto.request.FeatureRequest;
import com.fisport.dto.response.FeatureResponse;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Feature;
import com.fisport.repository.FeatureRepository;
import com.fisport.service.FeatureService;
import com.fisport.util.SlugUtils;
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

    @Override
    public void addFeature(FeatureRequest request) {
        featureRepository.findByName(request.getName()).ifPresent(f -> {
            throw new InvalidDataException("Tiện ích đã tồn tại");
        });

        String slug = SlugUtils.slugify(request.getName());

        featureRepository.save(Feature.builder()
                .name(request.getName())
                .slug(slug)
                .build());
    }

    @Override
    public void deleteFeature(Long id) {
        Feature field = featureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiện ích"));
        featureRepository.deleteById(id);
    }
}
