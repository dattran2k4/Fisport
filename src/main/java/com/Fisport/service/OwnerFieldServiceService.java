package com.Fisport.service;

import com.Fisport.dto.response.OwnerFieldServiceResponse;

import java.util.List;

public interface OwnerFieldServiceService {
    List<OwnerFieldServiceResponse> getAllFieldServicesByOwner(Long ownerId);
}




