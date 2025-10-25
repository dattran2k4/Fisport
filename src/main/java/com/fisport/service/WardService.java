package com.fisport.service;

import com.fisport.dto.response.WardResponse;

import java.util.List;

public interface WardService {

    List<WardResponse> getAllWardsByCityId(long cityId);
    WardResponse getWardBySlug(String slug);
}
