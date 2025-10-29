package com.fisport.service.impl;

import com.fisport.dto.response.ChallengeMatchTypeResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.ChallengeMatchType;
import com.fisport.model.FieldType;
import com.fisport.repository.ChallengeMatchTypeRepository;
import com.fisport.repository.FieldTypeRepository;
import com.fisport.service.ChallengeMatchTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeMatchTypeServiceImpl implements ChallengeMatchTypeService {

    private final ChallengeMatchTypeRepository challengeMatchTypeRepository;
    private final FieldTypeRepository fieldTypeRepository;

    @Override
    public Integer maxPlayer(Long id) {
        ChallengeMatchType c = challengeMatchTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Challenge Match Type Not Found"));

        return c.getMaxPlayers();
    }

    @Override
    public List<ChallengeMatchTypeResponse> getAllChallengeMatchTypesByFieldType(Long fieldTypeId) {
        FieldType fieldType = fieldTypeRepository.findById(fieldTypeId).orElseThrow(() -> new ResourceNotFoundException("Field Type Not Found"));

        List<ChallengeMatchType> matchTypes = fieldType.getChallengeMatchTypes();

        return matchTypes.stream().map(t -> ChallengeMatchTypeResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .build()).toList();
    }
}
