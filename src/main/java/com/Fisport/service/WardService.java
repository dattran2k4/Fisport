package com.Fisport.service;

import com.Fisport.dto.response.WardResponse;

import java.util.List;

public interface WardService {

    List<WardResponse> getAllWardsByCityId(long cityId);
}
