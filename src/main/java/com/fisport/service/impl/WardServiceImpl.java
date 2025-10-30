package com.fisport.service.impl;

import com.fisport.dto.response.CityResponse;
import com.fisport.dto.response.WardResponse;
import com.fisport.model.Ward;
import com.fisport.repository.WardRepository;
import com.fisport.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Override
    public List<WardResponse> getAllWardsByCityId(long cityId) {
        List<Ward> wards = wardRepository.findByCityId(cityId);
        return wards.stream()
                .map(w -> WardResponse.builder()
                        .id(w.getId())
                        .name(w.getName())
                        .slug(w.getSlug())
                        .cityResponse(CityResponse.builder()
                                .id(w.getCity().getId())
                                .name(w.getCity().getName())
                                .slug(w.getCity().getSlug())
                                .build())
                        .build())
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
