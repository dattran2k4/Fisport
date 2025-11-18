package com.fisport.service.impl;

import com.fisport.common.EFieldServiceItem;
import com.fisport.dto.request.OwnerServiceItemRequest;
import com.fisport.dto.request.OwnerServiceRequest;
import com.fisport.dto.response.OwnerServiceItemResponse;
import com.fisport.dto.response.OwnerServiceResponse;
import com.fisport.dto.response.OwnerServiceItemResponse.OwnerFieldServiceItemInfo;
import com.fisport.exception.OwnerAccessDeniedException;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Field;
import com.fisport.model.FieldServiceItem;
import com.fisport.model.Service;
import com.fisport.model.ServiceItem;
import com.fisport.model.User;
import com.fisport.repository.FieldRepository;
import com.fisport.repository.FieldServiceItemRepository;
import com.fisport.repository.ServiceItemRepository;
import com.fisport.repository.ServiceRepository;
import com.fisport.repository.UserRepository;
import com.fisport.service.OwnerServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation của OwnerServiceService
 * Đảm bảo Owner chỉ có thể CRUD dịch vụ/sản phẩm cho sân của mình
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OwnerServiceServiceImpl implements OwnerServiceService {

    // ========== Constants ==========
    private static final String ERROR_OWNER_NOT_FOUND = "Không tìm thấy owner với username: %s";
    private static final String ERROR_SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ với ID: %d";
    private static final String ERROR_SERVICE_ITEM_NOT_FOUND = "Không tìm thấy sản phẩm với ID: %d";
    private static final String ERROR_FIELD_ACCESS_DENIED = "Bạn không có quyền truy cập sân này";
    private static final String ERROR_SERVICE_NAME_EXISTS = "Dịch vụ với tên '%s' đã tồn tại";
    private static final String ERROR_SERVICE_ITEM_NAME_EXISTS = "Sản phẩm '%s' đã tồn tại trong loại dịch vụ này";
    private static final String ERROR_SERVICE_USED_BY_OTHERS = "Không thể xóa dịch vụ này vì đang được sử dụng bởi owner khác";
    private static final String ERROR_SERVICE_ITEM_EXISTS_IN_FIELD = "Sản phẩm này đã được thêm vào sân rồi";

    // ========== Dependencies ==========
    private final ServiceRepository serviceRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final FieldRepository fieldRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OwnerServiceResponse> getAllServicesByOwner(String ownerUsername) {
        User owner = getOwnerByUsername(ownerUsername);
        List<Field> ownerFields = fieldRepository.findByOwner(owner);
        
        if (ownerFields.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Lấy tất cả FieldServiceItems của owner
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(owner.getId());
        
        // Nhóm theo Service
        Set<Service> services = fieldServiceItems.stream()
                .map(fsi -> fsi.getServiceItem().getService())
                .collect(Collectors.toSet());
        
        return services.stream()
                .map(service -> toOwnerServiceResponse(service, owner.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerServiceResponse> searchServicesByOwner(String ownerUsername, String keyword) {
        User owner = getOwnerByUsername(ownerUsername);
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(owner.getId());
        
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim().toLowerCase();
        
        Set<Service> services = fieldServiceItems.stream()
                .map(fsi -> fsi.getServiceItem().getService())
                .filter(service -> service.getName().toLowerCase().contains(searchKeyword))
                .collect(Collectors.toSet());
        
        return services.stream()
                .map(service -> toOwnerServiceResponse(service, owner.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerServiceResponse getServiceByIdAndOwner(String ownerUsername, Long serviceId) {
        User owner = getOwnerByUsername(ownerUsername);
        Service service = findServiceById(serviceId);
        validateServiceAccess(owner.getId(), serviceId, "Bạn không có quyền truy cập dịch vụ này");
        return toOwnerServiceResponse(service, owner.getId());
    }

    @Override
    @Transactional
    public OwnerServiceResponse createService(String ownerUsername, OwnerServiceRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        
        // Kiểm tra tên dịch vụ đã tồn tại chưa
        serviceRepository.findByName(request.getName()).ifPresent(s -> {
            throw new InvalidDataException(String.format(ERROR_SERVICE_NAME_EXISTS, request.getName()));
        });
        
        Service service = Service.builder()
                .name(request.getName().trim())
                .build();
        
        Service savedService = serviceRepository.save(service);
        return toOwnerServiceResponse(savedService, owner.getId());
    }

    @Override
    @Transactional
    public OwnerServiceResponse updateService(String ownerUsername, Long serviceId, OwnerServiceRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        Service service = findServiceById(serviceId);
        validateServiceAccess(owner.getId(), serviceId, "Bạn không có quyền cập nhật dịch vụ này");
        validateServiceNameUniqueness(request.getName(), serviceId);
        
        service.setName(request.getName().trim());
        Service updatedService = serviceRepository.save(service);
        return toOwnerServiceResponse(updatedService, owner.getId());
    }

    @Override
    @Transactional
    public void deleteService(String ownerUsername, Long serviceId) {
        User owner = getOwnerByUsername(ownerUsername);
        Service service = findServiceById(serviceId);
        validateServiceAccess(owner.getId(), serviceId, "Bạn không có quyền xóa dịch vụ này");
        validateServiceNotUsedByOthers(owner.getId(), serviceId);
        serviceRepository.delete(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerServiceItemResponse> getAllServiceItemsByOwner(String ownerUsername) {
        User owner = getOwnerByUsername(ownerUsername);
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(owner.getId());
        
        // Nhóm theo ServiceItem
        Set<ServiceItem> serviceItems = fieldServiceItems.stream()
                .map(FieldServiceItem::getServiceItem)
                .collect(Collectors.toSet());
        
        return serviceItems.stream()
                .map(si -> toOwnerServiceItemResponse(si, owner.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerServiceItemResponse getServiceItemByIdAndOwner(String ownerUsername, Long serviceItemId) {
        User owner = getOwnerByUsername(ownerUsername);
        ServiceItem serviceItem = findServiceItemById(serviceItemId);
        validateServiceItemAccess(owner.getId(), serviceItemId, "Bạn không có quyền truy cập sản phẩm này");
        return toOwnerServiceItemResponse(serviceItem, owner.getId());
    }

    @Override
    @Transactional
    public OwnerServiceItemResponse createServiceItem(String ownerUsername, OwnerServiceItemRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        
        // Kiểm tra Service có tồn tại không
        Service service = findServiceById(request.getServiceId());
        
        // Kiểm tra Field có thuộc về owner không
        Field field = validateFieldOwnership(owner.getId(), request.getFieldId());
        
        // Kiểm tra tên sản phẩm đã tồn tại trong service này chưa
        serviceItemRepository.findByNameAndServiceId(request.getName(), request.getServiceId())
                .ifPresent(si -> {
                    throw new InvalidDataException(String.format(ERROR_SERVICE_ITEM_NAME_EXISTS, request.getName()));
                });
        
        // Tạo ServiceItem
        ServiceItem serviceItem = ServiceItem.builder()
                .name(request.getName().trim())
                .service(service)
                .build();
        
        ServiceItem savedServiceItem = serviceItemRepository.save(serviceItem);
        
        // Tạo FieldServiceItem để liên kết với field của owner
        FieldServiceItem fieldServiceItem = FieldServiceItem.builder()
                .field(field)
                .serviceItem(savedServiceItem)
                .price(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .status(EFieldServiceItem.ACTIVE)
                .build();
        
        fieldServiceItemRepository.save(fieldServiceItem);
        
        return toOwnerServiceItemResponse(savedServiceItem, owner.getId());
    }

    @Override
    @Transactional
    public OwnerServiceItemResponse updateServiceItem(String ownerUsername, Long serviceItemId, OwnerServiceItemRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        ServiceItem serviceItem = findServiceItemById(serviceItemId);
        validateServiceItemAccess(owner.getId(), serviceItemId, "Bạn không có quyền cập nhật sản phẩm này");
        
        Service service = findServiceById(request.getServiceId());
        validateServiceItemNameUniqueness(request.getName(), request.getServiceId(), serviceItemId);
        
        serviceItem.setName(request.getName().trim());
        serviceItem.setService(service);
        ServiceItem updatedServiceItem = serviceItemRepository.save(serviceItem);
        
        updateFieldServiceItemIfNeeded(owner, serviceItemId, request);
        
        return toOwnerServiceItemResponse(updatedServiceItem, owner.getId());
    }

    @Override
    @Transactional
    public void deleteServiceItem(String ownerUsername, Long serviceItemId) {
        User owner = getOwnerByUsername(ownerUsername);
        ServiceItem serviceItem = findServiceItemById(serviceItemId);
        validateServiceItemAccess(owner.getId(), serviceItemId, "Bạn không có quyền xóa sản phẩm này");
        
        deleteOwnerFieldServiceItems(owner.getId(), serviceItemId);
        
        if (!isServiceItemUsedByOthers(owner.getId(), serviceItemId)) {
            serviceItemRepository.delete(serviceItem);
        }
    }
    
    // ========== Helper Methods ==========
    
    /**
     * Lấy User (owner) theo username
     */
    private User getOwnerByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ERROR_OWNER_NOT_FOUND, username)));
    }
    
    /**
     * Tìm Service theo ID
     */
    private Service findServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ERROR_SERVICE_NOT_FOUND, serviceId)));
    }
    
    /**
     * Tìm ServiceItem theo ID
     */
    private ServiceItem findServiceItemById(Long serviceItemId) {
        return serviceItemRepository.findById(serviceItemId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ERROR_SERVICE_ITEM_NOT_FOUND, serviceItemId)));
    }
    
    /**
     * Kiểm tra field có thuộc về owner không
     */
    private Field validateFieldOwnership(Long ownerId, Long fieldId) {
        Field field = fieldRepository.findByOwnerIdAndId(ownerId, fieldId);
        if (field == null) {
            throw new OwnerAccessDeniedException(ERROR_FIELD_ACCESS_DENIED);
        }
        return field;
    }
    
    /**
     * Kiểm tra service có items liên kết với fields của owner không
     */
    private void validateServiceAccess(Long ownerId, Long serviceId, String errorMessage) {
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        boolean hasAccess = fieldServiceItems.stream()
                .anyMatch(fsi -> fsi.getServiceItem().getService().getId().equals(serviceId));
        
        if (!hasAccess) {
            throw new OwnerAccessDeniedException(errorMessage);
        }
    }
    
    /**
     * Kiểm tra service item có liên kết với fields của owner không
     */
    private void validateServiceItemAccess(Long ownerId, Long serviceItemId, String errorMessage) {
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        boolean hasAccess = fieldServiceItems.stream()
                .anyMatch(fsi -> fsi.getServiceItem().getId().equals(serviceItemId));
        
        if (!hasAccess) {
            throw new OwnerAccessDeniedException(errorMessage);
        }
    }
    
    /**
     * Kiểm tra tên service có trùng không (khi update)
     */
    private void validateServiceNameUniqueness(String name, Long serviceId) {
        serviceRepository.findByName(name).ifPresent(s -> {
            if (!s.getId().equals(serviceId)) {
                throw new InvalidDataException(String.format(ERROR_SERVICE_NAME_EXISTS, name));
            }
        });
    }
    
    /**
     * Kiểm tra tên service item có trùng không (khi update)
     */
    private void validateServiceItemNameUniqueness(String name, Long serviceId, Long serviceItemId) {
        serviceItemRepository.findByNameAndServiceId(name, serviceId).ifPresent(si -> {
            if (!si.getId().equals(serviceItemId)) {
                throw new InvalidDataException(String.format(ERROR_SERVICE_ITEM_NAME_EXISTS, name));
            }
        });
    }
    
    /**
     * Kiểm tra service không được sử dụng bởi owner khác
     * Note: Có thể tối ưu bằng cách tạo query riêng nếu cần
     */
    private void validateServiceNotUsedByOthers(Long ownerId, Long serviceId) {
        List<FieldServiceItem> allFieldServiceItems = fieldServiceItemRepository.findAll();
        boolean usedByOthers = allFieldServiceItems.stream()
                .anyMatch(fsi -> fsi.getServiceItem().getService().getId().equals(serviceId)
                        && !fsi.getField().getOwner().getId().equals(ownerId));
        
        if (usedByOthers) {
            throw new InvalidDataException(ERROR_SERVICE_USED_BY_OTHERS);
        }
    }
    
    /**
     * Kiểm tra service item đã tồn tại trong field chưa
     */
    private void validateServiceItemNotExistsInField(Long fieldId, Long serviceItemId) {
        List<FieldServiceItem> existing = fieldServiceItemRepository.findByFieldId(fieldId);
        boolean alreadyExists = existing.stream()
                .anyMatch(fsi -> fsi.getServiceItem().getId().equals(serviceItemId));
        
        if (alreadyExists) {
            throw new InvalidDataException(ERROR_SERVICE_ITEM_EXISTS_IN_FIELD);
        }
    }
    
    /**
     * Cập nhật FieldServiceItem nếu có thay đổi
     */
    private void updateFieldServiceItemIfNeeded(User owner, Long serviceItemId, OwnerServiceItemRequest request) {
        if (request.getFieldId() == null) {
            return;
        }
        
        List<FieldServiceItem> ownerFieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(owner.getId());
        FieldServiceItem existingFieldServiceItem = ownerFieldServiceItems.stream()
                .filter(fsi -> fsi.getServiceItem().getId().equals(serviceItemId))
                .findFirst()
                .orElse(null);
        
        if (existingFieldServiceItem != null) {
            Field field = validateFieldOwnership(owner.getId(), request.getFieldId());
            existingFieldServiceItem.setField(field);
            
            if (request.getPrice() != null) {
                existingFieldServiceItem.setPrice(request.getPrice());
            }
            if (request.getQuantity() != null) {
                existingFieldServiceItem.setQuantity(request.getQuantity());
            }
            
            fieldServiceItemRepository.save(existingFieldServiceItem);
        }
    }
    
    /**
     * Xóa các FieldServiceItem của owner liên kết với serviceItem
     */
    private void deleteOwnerFieldServiceItems(Long ownerId, Long serviceItemId) {
        List<FieldServiceItem> ownerFieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        List<FieldServiceItem> fieldServiceItemsToDelete = ownerFieldServiceItems.stream()
                .filter(fsi -> fsi.getServiceItem().getId().equals(serviceItemId))
                .collect(Collectors.toList());
        
        fieldServiceItemRepository.deleteAll(fieldServiceItemsToDelete);
    }
    
    /**
     * Kiểm tra service item có được sử dụng bởi owner khác không
     */
    private boolean isServiceItemUsedByOthers(Long ownerId, Long serviceItemId) {
        List<FieldServiceItem> allFieldServiceItems = fieldServiceItemRepository.findAll();
        return allFieldServiceItems.stream()
                .anyMatch(fsi -> fsi.getServiceItem().getId().equals(serviceItemId)
                        && !fsi.getField().getOwner().getId().equals(ownerId));
    }
    
    /**
     * Tạo FieldServiceItem mới
     */
    private FieldServiceItem createFieldServiceItem(Field field, ServiceItem serviceItem, 
                                                     BigDecimal price, Integer quantity) {
        return FieldServiceItem.builder()
                .field(field)
                .serviceItem(serviceItem)
                .price(price != null ? price : BigDecimal.ZERO)
                .quantity(quantity != null ? quantity : 0)
                .status(EFieldServiceItem.ACTIVE)
                .build();
    }
    
    /**
     * Convert FieldServiceItem sang OwnerFieldServiceResponse
     */
    private com.fisport.dto.response.OwnerFieldServiceResponse toOwnerFieldServiceResponse(FieldServiceItem fsi) {
        return com.fisport.dto.response.OwnerFieldServiceResponse.builder()
                .fieldServiceItemId(fsi.getId())
                .fieldId(fsi.getField().getId())
                .fieldName(fsi.getField().getName())
                .serviceItemId(fsi.getServiceItem().getId())
                .serviceItemName(fsi.getServiceItem().getName())
                .serviceName(fsi.getServiceItem().getService().getName())
                .price(fsi.getPrice())
                .quantity(fsi.getQuantity())
                .status(fsi.getStatus())
                .build();
    }
    
    private OwnerServiceResponse toOwnerServiceResponse(Service service, Long ownerId) {
        // Lấy tất cả FieldServiceItems của owner cho service này
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        
        // Lọc các ServiceItems thuộc service này
        Set<ServiceItem> serviceItems = fieldServiceItems.stream()
                .map(FieldServiceItem::getServiceItem)
                .filter(si -> si.getService().getId().equals(service.getId()))
                .collect(Collectors.toSet());
        
        List<OwnerServiceItemResponse> ownerServiceItemResponses = serviceItems.stream()
                .map(si -> toOwnerServiceItemResponse(si, ownerId))
                .collect(Collectors.toList());
        
        // Đếm số fields đang sử dụng service này
        Set<Long> fieldIds = fieldServiceItems.stream()
                .filter(fsi -> fsi.getServiceItem().getService().getId().equals(service.getId()))
                .map(fsi -> fsi.getField().getId())
                .collect(Collectors.toSet());
        
        return OwnerServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .serviceItems(ownerServiceItemResponses)
                .totalFields(fieldIds.size())
                .build();
    }
    
    private OwnerServiceItemResponse toOwnerServiceItemResponse(ServiceItem serviceItem, Long ownerId) {
        // Lấy tất cả FieldServiceItems của owner cho serviceItem này
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findAllByOwnerId(ownerId);
        
        List<OwnerFieldServiceItemInfo> fieldServiceItemInfos = fieldServiceItems.stream()
                .filter(fsi -> fsi.getServiceItem().getId().equals(serviceItem.getId()))
                .map(fsi -> OwnerFieldServiceItemInfo.builder()
                        .fieldServiceItemId(fsi.getId())
                        .fieldId(fsi.getField().getId())
                        .fieldName(fsi.getField().getName())
                        .price(fsi.getPrice())
                        .quantity(fsi.getQuantity())
                        .build())
                .collect(Collectors.toList());
        
        return OwnerServiceItemResponse.builder()
                .id(serviceItem.getId())
                .name(serviceItem.getName())
                .serviceId(serviceItem.getService().getId())
                .serviceName(serviceItem.getService().getName())
                .fieldServiceItems(fieldServiceItemInfos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.fisport.dto.response.OwnerFieldServiceResponse> getFieldServiceItemsByField(String ownerUsername, Long fieldId) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findByFieldId(fieldId);
        
        return fieldServiceItems.stream()
                .filter(fsi -> fsi.getField().getOwner().getId().equals(owner.getId()))
                .map(this::toOwnerFieldServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addServiceItemToField(String ownerUsername, Long fieldId, Long serviceItemId, BigDecimal price, Integer quantity) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        ServiceItem serviceItem = findServiceItemById(serviceItemId);
        validateServiceItemNotExistsInField(fieldId, serviceItemId);
        
        FieldServiceItem fieldServiceItem = createFieldServiceItem(field, serviceItem, price, quantity);
        fieldServiceItemRepository.save(fieldServiceItem);
    }

    @Override
    @Transactional
    public void deleteFieldServiceItem(String ownerUsername, Long fieldServiceItemId) {
        User owner = getOwnerByUsername(ownerUsername);
        
        // Lấy và kiểm tra FieldServiceItem
        FieldServiceItem fieldServiceItem = fieldServiceItemRepository.findById(fieldServiceItemId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ERROR_SERVICE_ITEM_NOT_FOUND, fieldServiceItemId)));
        
        // Kiểm tra field có thuộc về owner không
        if (!fieldServiceItem.getField().getOwner().getId().equals(owner.getId())) {
            throw new OwnerAccessDeniedException(ERROR_FIELD_ACCESS_DENIED);
        }
        
        // Xóa FieldServiceItem
        fieldServiceItemRepository.delete(fieldServiceItem);
    }
    
    // ========== New Methods for Refactored Flow ==========
    
    @Override
    @Transactional(readOnly = true)
    public List<com.fisport.dto.response.FieldResponse> getOwnerFields(String ownerUsername) {
        User owner = getOwnerByUsername(ownerUsername);
        List<Field> fields = fieldRepository.findByOwner(owner);
        return fields.stream()
                .map(field -> com.fisport.dto.response.FieldResponse.builder()
                        .id(field.getId())
                        .name(field.getName())
                        .address(field.getAddress())
                        .banner(field.getBanner())
                        .description(field.getDescription())
                        .slug(field.getSlug())
                        .openTime(field.getOpenTime())
                        .closeTime(field.getCloseTime())
                        .latitude(field.getLatitude())
                        .longitude(field.getLongitude())
                        .status(field.getFieldStatus())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OwnerServiceResponse> getServicesWithStatus(String ownerUsername, Long fieldId) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        
        // Đảm bảo field được load đầy đủ
        if (field.getName() == null) {
            field = fieldRepository.findById(fieldId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân với ID: " + fieldId));
        }
        
        String fieldName = field.getName() != null ? field.getName() : "Sân #" + fieldId;
        
        // Lấy tất cả services
        List<Service> allServices = serviceRepository.findAll();
        
        // Lấy tất cả FieldServiceItems của field này (có thể rỗng nếu chưa có item nào)
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findByFieldId(fieldId);
        
        return allServices.stream()
                .map(service -> {
                    // Kiểm tra service có enabled không (có ít nhất một FieldServiceItem ACTIVE)
                    boolean enabled = fieldServiceItems != null && !fieldServiceItems.isEmpty() && 
                            fieldServiceItems.stream()
                                    .anyMatch(fsi -> fsi.getServiceItem() != null 
                                            && fsi.getServiceItem().getService() != null
                                            && fsi.getServiceItem().getService().getId().equals(service.getId())
                                            && fsi.getStatus() == EFieldServiceItem.ACTIVE);
                    
                    // Lấy service items của service này trong field
                    List<FieldServiceItem> serviceFieldItems = (fieldServiceItems != null && !fieldServiceItems.isEmpty() 
                            ? fieldServiceItems 
                            : new ArrayList<FieldServiceItem>())
                            .stream()
                            .filter(fsi -> fsi.getServiceItem() != null 
                                    && fsi.getServiceItem().getService() != null
                                    && fsi.getServiceItem().getService().getId().equals(service.getId()))
                            .collect(Collectors.toList());
                    
                    List<OwnerServiceItemResponse> serviceItemResponses = serviceFieldItems.stream()
                            .map(fsi -> {
                                List<OwnerFieldServiceItemInfo> fieldServiceItemInfos = List.of(
                                        OwnerFieldServiceItemInfo.builder()
                                                .fieldServiceItemId(fsi.getId())
                                                .fieldId(fieldId)
                                                .fieldName(fieldName)
                                                .price(fsi.getPrice() != null ? fsi.getPrice() : BigDecimal.ZERO)
                                                .quantity(fsi.getQuantity() != null ? fsi.getQuantity() : 0)
                                                .build()
                                );
                                return OwnerServiceItemResponse.builder()
                                        .id(fsi.getServiceItem().getId())
                                        .name(fsi.getServiceItem().getName() != null ? fsi.getServiceItem().getName() : "")
                                        .serviceId(service.getId())
                                        .serviceName(service.getName() != null ? service.getName() : "")
                                        .fieldServiceItems(fieldServiceItemInfos)
                                        .build();
                            })
                            .collect(Collectors.toList());
                    
                    return OwnerServiceResponse.builder()
                            .id(service.getId())
                            .name(service.getName() != null ? service.getName() : "")
                            .enabled(enabled)
                            .serviceItems(serviceItemResponses)
                            .totalFields(1) // Chỉ tính field hiện tại
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void toggleService(String ownerUsername, Long fieldId, Long serviceId) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        Service service = findServiceById(serviceId);
        
        // Lấy tất cả FieldServiceItems của service này trong field
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findByFieldIdAndServiceId(fieldId, serviceId);
        
        if (fieldServiceItems.isEmpty()) {
            // Nếu chưa có FieldServiceItem nào, tạo một item mới với service item đầu tiên của service
            List<ServiceItem> serviceItems = serviceItemRepository.findByServiceId(serviceId);
            if (serviceItems.isEmpty()) {
                throw new InvalidDataException("Dịch vụ này chưa có sản phẩm nào. Vui lòng thêm sản phẩm trước.");
            }
            
            ServiceItem firstItem = serviceItems.get(0);
            FieldServiceItem newFieldServiceItem = createFieldServiceItem(field, firstItem, BigDecimal.ZERO, 0);
            fieldServiceItemRepository.save(newFieldServiceItem);
        } else {
            // Toggle status của tất cả FieldServiceItems
            boolean hasActive = fieldServiceItems.stream()
                    .anyMatch(fsi -> fsi.getStatus() == EFieldServiceItem.ACTIVE);
            
            EFieldServiceItem newStatus = hasActive ? EFieldServiceItem.INACTIVE : EFieldServiceItem.ACTIVE;
            fieldServiceItems.forEach(fsi -> fsi.setStatus(newStatus));
            fieldServiceItemRepository.saveAll(fieldServiceItems);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OwnerServiceItemResponse> getItemsByFieldAndService(String ownerUsername, Long fieldId, Long serviceId) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        Service service = findServiceById(serviceId);
        
        // Lấy tất cả service items của service
        List<ServiceItem> serviceItems = serviceItemRepository.findByServiceId(serviceId);
        
        // Lấy tất cả FieldServiceItems của field và service này
        List<FieldServiceItem> fieldServiceItems = fieldServiceItemRepository.findByFieldIdAndServiceId(fieldId, serviceId);
        
        return serviceItems.stream()
                .map(serviceItem -> {
                    // Tìm FieldServiceItem tương ứng
                    FieldServiceItem fieldServiceItem = fieldServiceItems.stream()
                            .filter(fsi -> fsi.getServiceItem().getId().equals(serviceItem.getId()))
                            .findFirst()
                            .orElse(null);
                    
                    List<OwnerFieldServiceItemInfo> fieldServiceItemInfos = fieldServiceItem != null
                            ? List.of(OwnerFieldServiceItemInfo.builder()
                                    .fieldServiceItemId(fieldServiceItem.getId())
                                    .fieldId(fieldId)
                                    .fieldName(field.getName())
                                    .price(fieldServiceItem.getPrice())
                                    .quantity(fieldServiceItem.getQuantity())
                                    .build())
                            : List.of();
                    
                    return OwnerServiceItemResponse.builder()
                            .id(serviceItem.getId())
                            .name(serviceItem.getName())
                            .serviceId(serviceId)
                            .serviceName(service.getName())
                            .fieldServiceItems(fieldServiceItemInfos)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OwnerServiceItemResponse createItem(String ownerUsername, Long fieldId, Long serviceId, OwnerServiceItemRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        Service service = findServiceById(serviceId);
        
        // Kiểm tra tên service item đã tồn tại chưa
        serviceItemRepository.findByNameAndServiceId(request.getName(), serviceId)
                .ifPresent(si -> {
                    throw new InvalidDataException(String.format(ERROR_SERVICE_ITEM_NAME_EXISTS, request.getName()));
                });
        
        // Tạo ServiceItem mới
        ServiceItem serviceItem = ServiceItem.builder()
                .name(request.getName().trim())
                .service(service)
                .build();
        ServiceItem savedServiceItem = serviceItemRepository.save(serviceItem);
        
        // Tạo FieldServiceItem
        FieldServiceItem fieldServiceItem = createFieldServiceItem(
                field, savedServiceItem,
                request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO,
                request.getQuantity() != null ? request.getQuantity() : 0
        );
        fieldServiceItemRepository.save(fieldServiceItem);
        
        return toOwnerServiceItemResponse(savedServiceItem, owner.getId());
    }
    
    @Override
    @Transactional
    public OwnerServiceItemResponse updateItem(String ownerUsername, Long itemId, Long fieldId, Long serviceId, OwnerServiceItemRequest request) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        Service service = findServiceById(serviceId);
        ServiceItem serviceItem = findServiceItemById(itemId);
        
        // Kiểm tra service item có thuộc service này không
        if (!serviceItem.getService().getId().equals(serviceId)) {
            throw new InvalidDataException("Sản phẩm không thuộc dịch vụ này");
        }
        
        // Kiểm tra tên service item đã tồn tại chưa (trừ chính nó)
        serviceItemRepository.findByNameAndServiceId(request.getName(), serviceId)
                .ifPresent(si -> {
                    if (!si.getId().equals(itemId)) {
                        throw new InvalidDataException(String.format(ERROR_SERVICE_ITEM_NAME_EXISTS, request.getName()));
                    }
                });
        
        // Cập nhật ServiceItem
        serviceItem.setName(request.getName().trim());
        ServiceItem updatedServiceItem = serviceItemRepository.save(serviceItem);
        
        // Cập nhật hoặc tạo FieldServiceItem
        fieldServiceItemRepository.findByFieldIdAndServiceItemId(fieldId, itemId)
                .ifPresentOrElse(
                        fsi -> {
                            fsi.setPrice(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO);
                            fsi.setQuantity(request.getQuantity() != null ? request.getQuantity() : 0);
                            fsi.setStatus(EFieldServiceItem.ACTIVE);
                            fieldServiceItemRepository.save(fsi);
                        },
                        () -> {
                            FieldServiceItem newFieldServiceItem = createFieldServiceItem(
                                    field, updatedServiceItem,
                                    request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO,
                                    request.getQuantity() != null ? request.getQuantity() : 0
                            );
                            fieldServiceItemRepository.save(newFieldServiceItem);
                        }
                );
        
        return toOwnerServiceItemResponse(updatedServiceItem, owner.getId());
    }
    
    @Override
    @Transactional
    public void deleteItem(String ownerUsername, Long itemId, Long fieldId, Long serviceId) {
        User owner = getOwnerByUsername(ownerUsername);
        Field field = validateFieldOwnership(owner.getId(), fieldId);
        ServiceItem serviceItem = findServiceItemById(itemId);
        
        // Kiểm tra service item có thuộc service này không
        if (!serviceItem.getService().getId().equals(serviceId)) {
            throw new InvalidDataException("Sản phẩm không thuộc dịch vụ này");
        }
        
        // Xóa FieldServiceItem
        fieldServiceItemRepository.findByFieldIdAndServiceItemId(fieldId, itemId)
                .ifPresent(fsi -> {
                    if (!fsi.getField().getOwner().getId().equals(owner.getId())) {
                        throw new OwnerAccessDeniedException(ERROR_FIELD_ACCESS_DENIED);
                    }
                    fieldServiceItemRepository.delete(fsi);
                });
        
        // Nếu service item không được sử dụng bởi owner khác, xóa luôn ServiceItem
        if (!isServiceItemUsedByOthers(owner.getId(), itemId)) {
            serviceItemRepository.delete(serviceItem);
        }
    }
    
}

