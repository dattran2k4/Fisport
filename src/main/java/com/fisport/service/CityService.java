package com.fisport.service;

import com.fisport.dto.response.CityResponse;

import java.util.List;

public interface CityService {
    List<CityResponse> findAll();
}
