package com.fisport.service;

import com.fisport.dto.request.FeatureRequest;
import com.fisport.dto.response.FeatureResponse;

import java.util.List;

public interface FeatureService {
    List<FeatureResponse> getListFeatures();
    void addFeature(FeatureRequest request);
    void deleteFeature(Long id);
}
