package com.Fisport.service.impl;

import com.Fisport.dto.response.WardResponse;
import com.Fisport.model.Ward;
import com.Fisport.repository.WardRepository;
import com.Fisport.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Override
    public List<WardResponse> getAllWardsByCityId(long cityId) {
        List<Ward>  wards = wardRepository.findByCityId(cityId);
        return wards.stream()
                .map(w -> new WardResponse(w.getId(), w.getName(), w.getSlug()))
                .toList();
    }

    @Override
    public WardResponse getWardBySlug(String slug) {
        Ward ward = wardRepository.findBySlug(slug);
        return WardResponse.builder()
                .id(ward.getId())
                .name(ward.getName())
                .slug(slug).build();
    }
}
