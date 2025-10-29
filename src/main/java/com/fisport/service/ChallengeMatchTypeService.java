package com.fisport.service;

import com.fisport.dto.response.ChallengeMatchTypeResponse;

import java.util.List;

public interface ChallengeMatchTypeService {
    Integer maxPlayer(Long id);

    List<ChallengeMatchTypeResponse> getAllChallengeMatchTypesByFieldType(Long fieldTypeId);

}
