package com.Fisport.service.impl;

import com.Fisport.dto.response.CityResponse;
import com.Fisport.dto.response.WardResponse;
import com.Fisport.model.City;
import com.Fisport.repository.CityRepository;
import com.Fisport.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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
