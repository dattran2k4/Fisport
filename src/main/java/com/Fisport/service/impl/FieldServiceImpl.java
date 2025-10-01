package com.Fisport.service.impl;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.FieldHasTimeSlotResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.dto.response.WardResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.FieldService;
import com.Fisport.util.EFieldStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final WardRepository wardRepository;
    private final UserRepository userRepository;
    private final FieldTypeRepository fieldTypeRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final FieldHasTimeSlotRepository fieldHasTimeSlotRepository;

    @Override
    public List<FieldResponse> getFieldByWardAndType(long wardId, long fieldTypeId) {
        List<Field> fields = fieldRepository.findByWardIdAndFieldTypeId(wardId, fieldTypeId);
        return fields.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<FieldResponse> getFieldByFieldTypeId(long fieldTypeId) {
        List<Field> fields = fieldRepository.findByFieldTypeId(fieldTypeId);
        return fields.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<FieldResponse> getFieldByOwnerId(Long ownerId) {
        List<Field> fields = fieldRepository.findByOwnerId(ownerId);
        return fields.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void createFieldByOwnerId(FieldRequest fieldRequest, Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Ward ward = wardRepository.findById(fieldRequest.getWardId()).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        FieldType fieldType = fieldRepository.findById(fieldRequest.getFieldTypeId()).orElseThrow(() -> new ResourceNotFoundException("Field type not found")).getFieldType();
        fieldRepository.save(Field.builder()
                .name(fieldRequest.getName())
                .banner(fieldRequest.getBanner())
                .address(fieldRequest.getAddress())
                .slug(fieldRequest.getSlug())
                .ward(ward)
                .owner(user)
                .fieldType(fieldType)
                .description(fieldRequest.getDescription())
                .fieldStatus(EFieldStatus.INACTIVE)
                .build());
    }


    @Override
    public void updateFieldByOwnerId(FieldRequest fieldRequest, Long ownerId, Long fieldId) {
        Field field = getFieldByid(fieldId);
        User user = userRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Ward ward = wardRepository.findById(fieldRequest.getWardId()).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        FieldType fieldType = fieldRepository.findById(fieldRequest.getFieldTypeId()).orElseThrow(() -> new ResourceNotFoundException("Field type not found")).getFieldType();
        field.setName(fieldRequest.getName());
        field.setAddress(fieldRequest.getAddress());
        field.setSlug(fieldRequest.getSlug());
        field.setBanner(fieldRequest.getBanner());
        field.setDescription(fieldRequest.getDescription());
        field.setWard(ward);
        field.setFieldType(fieldType);
        fieldRepository.save(field);
    }

    @Override
    public void changeStatusFieldByOwnerId(Long ownerId, Long fieldId, EFieldStatus fieldStatus) {
        Field field = getFieldByid(fieldId);
        field.setFieldStatus(fieldStatus);
        fieldRepository.save(field);
    }

    private Field getFieldByid(Long fieldId) {
        return fieldRepository.findById(fieldId).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
    }

    @Override
    public FieldResponse getField(Long fieldId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        return toDto(field);
    }

    @Override
    public List<FieldHasTimeSlotResponse> getTimeSlotAndPriceByFieldId(Long id) {
        Field field = getFieldByid(id);
        List<FieldHasTimeSlot> fieldHasTimeSlots = fieldHasTimeSlotRepository.findByFieldId(id);
        return fieldHasTimeSlots.stream()
                .map(fieldHasTimeSlot -> FieldHasTimeSlotResponse.builder()
                .id(fieldHasTimeSlot.getId())
                .startTime(fieldHasTimeSlot.getTimeSlot().getStartTime())
                .price(fieldHasTimeSlot.getPrice())
                .build()).toList();
    }


    private FieldTypeResponse toFieldTypeResponseDto(FieldType fieldType) {
        return FieldTypeResponse.builder()
                .id(fieldType.getId())
                .name(fieldType.getName())
                .slug(fieldType.getSlug())
                .build();
    }

    private WardResponse toWardDto(Ward ward) {
        return WardResponse.builder()
                .id(ward.getId())
                .name(ward.getName())
                .slug(ward.getSlug())
                .build();
    }




    private FieldResponse toDto(Field f) {
        return new FieldResponse(
                f.getId(),
                f.getName(),
                f.getAddress(),
                f.getBanner(),
                f.getSlug(),
                f.getDescription(),
                f.getOpenTime(),
                f.getCloseTime(),
                f.getFieldStatus(),
                new WardResponse(
                        f.getWard().getId(),
                        f.getWard().getName(),
                        f.getWard().getSlug()
                ),
                new FieldTypeResponse(
                        f.getFieldType().getId(),
                        f.getFieldType().getName(),
                        f.getFieldType().getSlug()
                )
        );
    }
}
