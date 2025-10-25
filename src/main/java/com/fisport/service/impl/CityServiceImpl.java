package com.fisport.service.impl;

import com.fisport.dto.response.CityResponse;
import com.fisport.dto.response.WardResponse;
import com.fisport.model.City;
import com.fisport.repository.CityRepository;
import com.fisport.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityResponse> findAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(c -> CityResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .wardResponses(c.getWards().stream().map(w -> WardResponse.builder()
                        .id(w.getId())
                        .name(w.getName())
                        .slug(w.getSlug())
                        .build()).collect(Collectors.toSet()))
                .build()).toList();
    }
}
