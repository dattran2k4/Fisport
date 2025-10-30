package com.fisport.service;

import com.fisport.dto.response.OwnerFieldServiceResponse;

import java.util.List;

public interface OwnerFieldServiceService {
    List<OwnerFieldServiceResponse> getAllFieldServicesByOwner(Long ownerId);
}




