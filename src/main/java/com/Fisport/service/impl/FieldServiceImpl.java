package com.Fisport.service.impl;

import com.Fisport.dto.request.FieldCreateRequest;
import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.response.*;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.FieldService;
import com.Fisport.common.EFieldStatus;
import com.Fisport.service.FieldSpecification;
import com.Fisport.util.SlugUtils;
import jakarta.transaction.Transactional;
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
    private final FieldHasFeatureRepository fieldHasFeatureRepository;
    private final ReviewRepository reviewRepository;
    private final FeatureRepository featureRepository;

    @Override
    public List<FieldResponse> getAllFields(Long wardId, Long fieldTypeId, EFieldStatus status, String keyword, Long... featureIds) {
        Specification<Field> specification = FieldSpecification.filterFields(wardId, fieldTypeId, status, keyword, featureIds);
        List<Field> fieds = fieldRepository.findAll(specification);
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
    @Transactional
    public void createFieldByOwner(FieldCreateRequest fieldCreateRequest, String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Ward ward = wardRepository.findById(Long.parseLong(fieldCreateRequest.getWard()))
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found"));

        FieldType fieldType = fieldRepository.findById(Long.parseLong(fieldCreateRequest.getField_type()))
                .orElseThrow(() -> new ResourceNotFoundException("Field type not found")).getFieldType();


        if (!fieldCreateRequest.getOpeningTime().isBefore(fieldCreateRequest.getClosingTime())) {
            throw new IllegalArgumentException("Giờ mở cửa phải sớm hơn giờ đóng cửa");
        }
        String slug = SlugUtils.slugify(fieldCreateRequest.getName());
        Field field = Field.builder()
                .name(fieldCreateRequest.getName())
                .banner(fieldCreateRequest.getBanner())
                .address(fieldCreateRequest.getAddress())
                .slug(slug)
                .openTime(fieldCreateRequest.getOpeningTime())
                .closeTime(fieldCreateRequest.getClosingTime())
                .fieldStatus(EFieldStatus.INACTIVE)
                .ward(ward)
                .longitude(Double.parseDouble(fieldCreateRequest.getLongitude()))
                .latitude(Double.parseDouble(fieldCreateRequest.getLatitude()))
                .owner(user)
                .fieldType(fieldType)
                .description(fieldCreateRequest.getDescription())
                .build();
        fieldRepository.save(field);
        if (fieldCreateRequest.getFeatures() != null && !fieldCreateRequest.getFeatures().isEmpty()) {

            List<Long> featureIds = fieldCreateRequest.getFeatures()
                    .stream()
                    .map(Long::parseLong)
                    .toList();

            List<Feature> features = featureRepository.findAllById(featureIds);
            List<FieldHasFeature> fieldFeatures = features.stream()
                    .map(feature -> FieldHasFeature.builder()
                            .feature(feature)
                            .field(field)
                            .build())
                    .toList();

            fieldHasFeatureRepository.saveAll(fieldFeatures);
        }
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
    public List<FieldResponse> getMyOwnerFields(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        List<Field> fields = fieldRepository.findByOwner(user);
        return fields.stream().map(this::toDto).toList();
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
        List<Field> fields = fieldRepository.findByFieldStatus(EFieldStatus.PENDING);
        return fields.stream().map(this::toDto).toList();
    }

    @Override
    public List<FieldResponse> getAllPendingFieldsByOwner(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chủ sân"));

        List<Field> fields = fieldRepository.findByFieldStatusAndOwner_Username(EFieldStatus.PENDING, user.getUsername());
        return fields.stream().map(this::toDto).toList();
    }

    @Override
    public FieldDetailResponse findBySlug(String fieldNameSlug) {
        Field field = fieldRepository.findBySlug(fieldNameSlug);
        Set<Feature> features = fieldHasFeatureRepository.findFeaturesByFieldId(field.getId());
        Double rating = reviewRepository.findAverageByFieldId(field.getId());
        return FieldDetailResponse.builder()
                .id(field.getId())
                .name(field.getName())
                .latitude(field.getLatitude())
                .longitude(field.getLongitude())
                .address(field.getAddress())
                .banner(field.getBanner())
                .slug(field.getSlug())
                .openTime(field.getOpenTime())
                .closeTime(field.getCloseTime())
                .description(field.getDescription())
                .typeSlug(field.getFieldType().getSlug())
                .city(field.getWard().getCity().getName())
                .ward(field.getWard().getName())
                .type(field.getFieldType().getName())
                .typeId(field.getFieldType().getId())
                .features(features.stream().map(Feature::getName).collect(Collectors.toSet()))
                .rating(rating)
                .build();
    }

    @Override
    public List<FieldDetailResponse> getFieldsNearBy(Double lat, Double lng, Double radius) {
        // 1° latitude ≈ 111 km
        Double latDistance = radius / 111.0;
        Double lngDistance = radius / (111.0 * Math.cos(Math.toRadians(lat)));

        Double minLat = lat - latDistance;
        Double maxLat = lat + latDistance;
        Double minLng = lng - lngDistance;
        Double maxLng = lng + lngDistance;

        //Bounding box radius
        List<Field> fields = fieldRepository.findFieldWithinRadius(lat, lng, minLat, maxLat, minLng, maxLng, radius);

        return fields.stream().map(f -> FieldDetailResponse.builder()
                .id(f.getId())
                .name(f.getName())
                .slug(f.getSlug())
                .city(f.getWard().getCity().getName())
                .ward(f.getWard().getName())
                .type(f.getFieldType().getName())
                .typeId(f.getFieldType().getId())
                .address(f.getAddress())
                .description(f.getDescription())
                .latitude(f.getLatitude())
                .longitude(f.getLongitude())
                .typeSlug(f.getFieldType().getSlug())
                .banner(f.getBanner())
                .rating(reviewRepository.findAverageByFieldId(f.getId()))
                .openTime(f.getOpenTime())
                .closeTime(f.getCloseTime())
                .features(f.getFeatures().stream().map(Feature::getName).collect(Collectors.toSet()))
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
        return FieldResponse.builder()
                .id(f.getId())
                .name(f.getName())
                .address(f.getAddress())
                .banner(f.getBanner())
                .slug(f.getSlug())
                .openTime(f.getOpenTime())
                .closeTime(f.getCloseTime())
                .status(f.getFieldStatus())
                .rating(reviewRepository.findAverageByFieldId(f.getId()))
                .wardResponse(toWardDto(f.getWard()))
                .fieldTypeResponse(toFieldTypeResponseDto(f.getFieldType()))
                .build();
    }
}
