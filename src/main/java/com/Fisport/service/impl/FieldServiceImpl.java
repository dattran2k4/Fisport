package com.Fisport.service.impl;

import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.*;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.FieldService;
import com.Fisport.common.EFieldStatus;
import com.Fisport.service.FieldSpecification;
import com.Fisport.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final WardRepository wardRepository;
    private final UserRepository userRepository;
    private final FieldTypeRepository fieldTypeRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final FieldHasTimeSlotRepository fieldHasTimeSlotRepository;
    private final FieldHasFeatureRepository fieldHasFeatureRepository;

    @Override
    public List<FieldResponse> getAllFields(Long wardId, Long fieldTypeId, EFieldStatus status, String keyword, String username, Long... featureIds) {
        Specification<Field> specification = FieldSpecification.filterFields(wardId, fieldTypeId, status, keyword, username, featureIds);
        List<Field> fieds = fieldRepository.findAll(specification);
        System.out.println(fieds);
        return fieds.stream().map(this::toDto).toList();
    }

//    @Override
//    public List<FieldResponse> getFieldByFieldTypeId(long fieldTypeId) {
//        List<Field> fields = fieldRepository.findByFieldTypeId(fieldTypeId);
//        return fields.stream()
//                .map(this::toDto)
//                .toList();
//    }

//    @Override
//    public List<FieldResponse> getFieldByOwner(String name) {
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
//
//        List<Field> fields = fieldRepository.findByOwnerId(user.getId());
//        return fields.stream()
//                .map(this::toDto)
//                .toList();
//    }

    @Override
    public void createFieldByOwnerId(FieldRequest fieldRequest, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Ward ward = wardRepository.findById(fieldRequest.getWardId()).orElseThrow(() -> new ResourceNotFoundException("Ward not found"));
        FieldType fieldType = fieldRepository.findById(fieldRequest.getFieldTypeId()).orElseThrow(() -> new ResourceNotFoundException("Field type not found")).getFieldType();

        String slug = SlugUtils.slugify(fieldRequest.getName());

        if (!fieldRequest.getOpenTime().isBefore(fieldRequest.getCloseTime())) {
            throw new IllegalArgumentException("Giờ mở cửa phải sớm hơn giờ đóng cửa");
        }

        fieldRepository.save(Field.builder()
                .name(fieldRequest.getName())
                .banner(fieldRequest.getBanner())
                .address(fieldRequest.getAddress())
                .slug(slug)
                .openTime(fieldRequest.getOpenTime())
                .closeTime(fieldRequest.getCloseTime())
                .fieldStatus(EFieldStatus.INACTIVE)
                .ward(ward)
                .owner(user)
                .fieldType(fieldType)
                .description(fieldRequest.getDescription())
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
        field.setSlug(SlugUtils.slugify(fieldRequest.getName()));
        field.setBanner(fieldRequest.getBanner());
        field.setDescription(fieldRequest.getDescription());
        field.setOpenTime(fieldRequest.getOpenTime());
        field.setCloseTime(fieldRequest.getCloseTime());
        field.setWard(ward);
        field.setFieldType(fieldType);
        fieldRepository.save(field);
    }

    @Override
    public void changeStatusFieldByAdmin(Long fieldId, EFieldStatus fieldStatus) {
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

    @Override
    public Set<FeatureResponse> getFeautresByField(Long id) {
        Set<Feature> feature = fieldHasFeatureRepository.findFeaturesByFieldId(id);
        return feature.stream().map(f -> FeatureResponse.builder()
                .id(f.getId())
                .name(f.getName())
                .slug(f.getSlug())
                .build()).collect(Collectors.toSet());
    }

    @Override
    public List<FieldResponse> getAllPendingFields() {
        List<Field>  fields = fieldRepository.findByFieldStatus(EFieldStatus.PENDING);
        return fields.stream().map(this::toDto).toList();
    }

    @Override
    public List<FieldResponse> getAllPendingFieldsByOwner(String name) {
        User user =  userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chủ sân"));

        List<Field>  fields = fieldRepository.findByFieldStatusAndOwner_Username(EFieldStatus.PENDING, name);
        return fields.stream().map(this::toDto).toList();
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
