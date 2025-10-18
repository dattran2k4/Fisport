package com.Fisport.service;

import com.Fisport.dto.response.CityResponse;

import java.util.List;

public interface CityService {
    List<CityResponse> findAll();
}
