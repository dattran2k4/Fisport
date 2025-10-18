package com.Fisport.service;

import com.Fisport.dto.request.FeatureRequest;
import com.Fisport.dto.response.FeatureResponse;

import java.util.List;

public interface FeatureService {
    List<FeatureResponse> getListFeatures();
    void addFeature(FeatureRequest request);
    void deleteFeature(Long id);
}
