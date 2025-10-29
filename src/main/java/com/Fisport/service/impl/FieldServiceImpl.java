package com.Fisport.service.impl;

import com.Fisport.common.EFieldServiceItem;
import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.FieldCreateRequest;
import com.Fisport.dto.request.FieldRequest;
import com.Fisport.dto.request.ServiceItemsRequest;
import com.Fisport.dto.response.*;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.FieldService;
import com.Fisport.common.EFieldStatus;
import com.Fisport.service.FieldSpecification;
import com.Fisport.util.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    private final ServiceItemRepository serviceItemRepository;
    private final SubFieldRepository subFieldRepository;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final FieldTypeRepository fieldTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    //    @Override
//    @Transactional
//    public void createFieldByOwner(FieldCreateRequest req, String username) {
//        User owner = getUser(username);
//        Ward ward = getWard(req.getWard());
//        FieldType fieldType = getFieldType(req.getField_type());
//
//        validateOpeningHours(req);
//
//        Field field = buildField(req, owner, ward, fieldType);
//        fieldRepository.save(field);
//
//        saveFeatures(req, field);
//        saveSubFields(req, field);
//        saveServiceItems(req, field);
//    }
    @Override
    @Transactional
    public void createFieldByOwner(FieldCreateRequest fieldCreateRequest, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Ward ward = wardRepository.findById(Long.parseLong(fieldCreateRequest.getWard())).orElseThrow(() -> new ResourceNotFoundException("Ward not found"));
        FieldType fieldType = fieldRepository.findById(Long.parseLong(fieldCreateRequest.getField_type())).orElseThrow(() -> new ResourceNotFoundException("Field type not found")).getFieldType();
        if (!fieldCreateRequest.getOpeningTime().isBefore(fieldCreateRequest.getClosingTime())) {
            throw new IllegalArgumentException("Giờ mở cửa phải sớm hơn giờ đóng cửa");
        }
        String slug = SlugUtils.slugify(fieldCreateRequest.getName());
        Field field = Field.builder().name(fieldCreateRequest
                        .getName())
                .banner(fieldCreateRequest.getBanner())
                .address(fieldCreateRequest.getAddress())
                .slug(slug)
                .openTime(fieldCreateRequest.getOpeningTime())
                .closeTime(fieldCreateRequest.getClosingTime())
                .fieldStatus(EFieldStatus.INACTIVE)
                .ward(ward).longitude(Double.parseDouble(fieldCreateRequest.getLongitude()))
                .latitude(Double.parseDouble(fieldCreateRequest.getLatitude()))
                .owner(user).fieldType(fieldType)
                .description(fieldCreateRequest.getDescription())
                .build();
        fieldRepository.save(field);
        if (fieldCreateRequest.getFeatures() != null && !fieldCreateRequest.getFeatures().isEmpty()) {
            List<Long> featureIds = fieldCreateRequest.getFeatures().stream().map(Long::parseLong).toList();
            List<Feature> features = featureRepository.findAllById(featureIds);
            List<FieldHasFeature> fieldFeatures = features.stream().map(feature -> FieldHasFeature.builder().feature(feature).field(field).build()).toList();
            fieldHasFeatureRepository.saveAll(fieldFeatures);
        }
        if (fieldCreateRequest.getSub_fields() != null && !fieldCreateRequest.getSub_fields().isEmpty()) {
            List<SubField> subFields = fieldCreateRequest.getSub_fields().stream().map(sub_field -> SubField.builder().field(field).name(sub_field).status(ESubFieldStatus.AVAILABLE).build()).toList();
            subFieldRepository.saveAll(subFields);
        }
        if (fieldCreateRequest.getServiceItems() != null && !fieldCreateRequest.getServiceItems().isEmpty()) {
            List<Long> serviceItemIds = fieldCreateRequest.getServiceItems().stream().map(ServiceItemsRequest::getId).toList();
            List<ServiceItem> serviceItems = serviceItemRepository.findAllById(serviceItemIds);
            Map<Long, ServiceItem> serviceItemMap = serviceItems.stream().collect(Collectors.toMap(ServiceItem::getId, s -> s));
            List<FieldServiceItem> fieldServiceItems = fieldCreateRequest.getServiceItems().stream().map(reqItem -> {
                FieldServiceItem fsi = new FieldServiceItem();
                fsi.setServiceItem(serviceItemMap.get(reqItem.getId()));
                fsi.setPrice(BigDecimal.valueOf(reqItem.getPrice()));
                fsi.setQuantity(reqItem.getQuantity());
                fsi.setField(field);
                fsi.setStatus(EFieldServiceItem.ACTIVE);
                return fsi;
            }).toList();
            fieldServiceItemRepository.saveAll(fieldServiceItems);
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

    @Transactional
    @Override
    public void deleteFieldByOwner(Long fieldId, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));

        if (!field.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa sân này!");
        }

        fieldHasFeatureRepository.deleteAllByField(field);
        fieldServiceItemRepository.deleteAllByField_Id(field.getId());
        subFieldRepository.deleteAllByField(field);
        reviewRepository.deleteByFieldId(field.getId());

        String oldImagePath = new File("src/main/resources/static" + field.getBanner()).getAbsolutePath();
        File oldFile = new File(oldImagePath);
        if (oldFile.exists()) {
            boolean deleted = oldFile.delete();
        }

        fieldRepository.deleteOneById(field.getId());

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

    @Override
    public FieldResponse getFieldByIdAndOwnerName(Long id, String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Field _f = fieldRepository.findByOwnerIdAndId(user.getId(), id);
        return toDto(_f);
    }
    @Override
    @Transactional
    public void updateFieldByOwner(FieldCreateRequest fieldCreateRequest, String username, Long id) {
        User owner = userRepository.findByUsername(username) .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Field field = fieldRepository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        Ward ward = wardRepository.findById(Long.parseLong(fieldCreateRequest.getWard()))
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found"));
        FieldType fieldType = fieldTypeRepository.findById(Long.parseLong(fieldCreateRequest.getField_type()))
                .orElseThrow(() -> new ResourceNotFoundException("Field type not found"));
        if (!fieldCreateRequest.getOpeningTime().isBefore(fieldCreateRequest.getClosingTime()))
            {   throw new IllegalArgumentException("Giờ mở cửa phải sớm hơn giờ đóng cửa"); }
        // Update thông tin cơ bản
        field.setName(fieldCreateRequest.getName());
        field.setAddress(fieldCreateRequest.getAddress());
        field.setDescription(fieldCreateRequest.getDescription());
        field.setOpenTime(fieldCreateRequest.getOpeningTime());
        field.setCloseTime(fieldCreateRequest.getClosingTime());
        field.setWard(ward);
        field.setLatitude(Double.parseDouble(fieldCreateRequest.getLatitude()));
        field.setLongitude(Double.parseDouble(fieldCreateRequest.getLongitude()));
        field.setFieldType(fieldType);
        field.setSlug(SlugUtils.slugify(fieldCreateRequest.getName()));
        // Nếu có ảnh mới thì update
        if (fieldCreateRequest.getBanner() != null && !fieldCreateRequest.getBanner().isEmpty()) {
            String oldImagePath = new File("src/main/resources/static" + field.getBanner()).getAbsolutePath();
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
            }
            field.setBanner(fieldCreateRequest.getBanner());
        }

        fieldRepository.save(field);

        // --- Cập nhật Features ---
        fieldHasFeatureRepository.deleteAllByField(field);
        entityManager.flush();
        entityManager.clear();

        if (fieldCreateRequest.getFeatures() != null && !fieldCreateRequest.getFeatures().isEmpty()) {
            List<Long> featureIds = fieldCreateRequest.getFeatures().stream().map(Long::parseLong).toList();
            List<Feature> features = featureRepository.findAllById(featureIds);
            List<FieldHasFeature> newFeatures = features.stream()
                    .map(f -> FieldHasFeature.builder().feature(f).field(field).build())
                    .toList();
            fieldHasFeatureRepository.saveAll(newFeatures);
        }

        // --- Cập nhật SubFields ---
        subFieldRepository.deleteAllByField(field);
        entityManager.flush();
        entityManager.clear();

        if (fieldCreateRequest.getSub_fields() != null && !fieldCreateRequest.getSub_fields().isEmpty()) {
            List<SubField> newSubFields = fieldCreateRequest.getSub_fields().stream()
                    .map(name -> SubField.builder()
                            .field(field)
                            .name(name)
                            .status(ESubFieldStatus.AVAILABLE)
                            .build())
                    .toList();
            subFieldRepository.saveAll(newSubFields);
        }

        // --- Cập nhật Service Items ---
        fieldServiceItemRepository.deleteAllByField_Id(field.getId());
        entityManager.flush();
        entityManager.clear();

        if (fieldCreateRequest.getServiceItems() != null && !fieldCreateRequest.getServiceItems().isEmpty()) {
            List<Long> serviceItemIds = fieldCreateRequest.getServiceItems().stream()
                    .map(ServiceItemsRequest::getId)
                    .toList();

            List<ServiceItem> serviceItems = serviceItemRepository.findAllById(serviceItemIds);
            Map<Long, ServiceItem> serviceItemMap = serviceItems.stream()
                    .collect(Collectors.toMap(ServiceItem::getId, s -> s, (a, b) -> a)); // tránh duplicate key

            List<FieldServiceItem> fieldServiceItems = fieldCreateRequest.getServiceItems().stream().map(reqItem -> {
                FieldServiceItem fsi = new FieldServiceItem();
                fsi.setServiceItem(serviceItemMap.get(reqItem.getId()));
                fsi.setPrice(BigDecimal.valueOf(reqItem.getPrice()));
                fsi.setQuantity(reqItem.getQuantity());
                fsi.setField(field);
                fsi.setStatus(EFieldServiceItem.ACTIVE);
                return fsi;
            }).toList();

            fieldServiceItemRepository.saveAll(fieldServiceItems);
        }
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
                .cityResponse(CityResponse.builder()
                        .name(ward.getCity().getName())
                        .id(ward.getCity().getId())
                        .build())
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
                .description(f.getDescription())
                .fieldTypeId(Long.toString(f.getFieldType().getId()))
                .closeTime(f.getCloseTime())
                .status(f.getFieldStatus())
                .latitude(f.getLatitude())
                .longitude(f.getLongitude())
                .rating(reviewRepository.findAverageByFieldId(f.getId()))
                .wardResponse(toWardDto(f.getWard()))
                .fieldTypeResponse(toFieldTypeResponseDto(f.getFieldType()))
                .build();
    }
}
