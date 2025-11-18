package com.fisport.service;

import com.fisport.dto.request.OwnerServiceItemRequest;
import com.fisport.dto.request.OwnerServiceRequest;
import com.fisport.dto.response.FieldResponse;
import com.fisport.dto.response.OwnerServiceItemResponse;
import com.fisport.dto.response.OwnerServiceResponse;

import java.util.List;

/**
 * Service interface cho quản lý dịch vụ/sản phẩm của Owner
 * Chỉ cho phép Owner CRUD dịch vụ/sản phẩm cho sân của mình
 */
public interface OwnerServiceService {
    
    // ========== New Methods for Refactored Flow ==========
    
    /**
     * Lấy danh sách fields của owner
     * @param ownerUsername Username của owner
     * @return Danh sách fields
     */
    List<FieldResponse> getOwnerFields(String ownerUsername);
    
    /**
     * Lấy danh sách tất cả services với trạng thái enabled/disabled cho field cụ thể
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @return Danh sách services với enabled status
     */
    List<OwnerServiceResponse> getServicesWithStatus(String ownerUsername, Long fieldId);
    
    /**
     * Bật/tắt service cho field
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @param serviceId ID của service
     */
    void toggleService(String ownerUsername, Long fieldId, Long serviceId);
    
    /**
     * Lấy danh sách items của service trong field cụ thể
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @param serviceId ID của service
     * @return Danh sách service items
     */
    List<OwnerServiceItemResponse> getItemsByFieldAndService(String ownerUsername, Long fieldId, Long serviceId);
    
    /**
     * Tạo service item mới cho field và service cụ thể
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @param serviceId ID của service
     * @param request Thông tin service item
     * @return Service item đã tạo
     */
    OwnerServiceItemResponse createItem(String ownerUsername, Long fieldId, Long serviceId, OwnerServiceItemRequest request);
    
    /**
     * Cập nhật service item
     * @param ownerUsername Username của owner
     * @param itemId ID của service item
     * @param fieldId ID của field
     * @param serviceId ID của service
     * @param request Thông tin cập nhật
     * @return Service item đã cập nhật
     */
    OwnerServiceItemResponse updateItem(String ownerUsername, Long itemId, Long fieldId, Long serviceId, OwnerServiceItemRequest request);
    
    /**
     * Xóa service item
     * @param ownerUsername Username của owner
     * @param itemId ID của service item
     * @param fieldId ID của field
     * @param serviceId ID của service
     */
    void deleteItem(String ownerUsername, Long itemId, Long fieldId, Long serviceId);
    
    // ========== Legacy Methods (kept for backward compatibility, may be deprecated) ==========
    
    /**
     * Lấy danh sách tất cả services có items được sử dụng bởi fields của owner
     * @param ownerUsername Username của owner
     * @return Danh sách services
     */
    List<OwnerServiceResponse> getAllServicesByOwner(String ownerUsername);
    
    /**
     * Tìm kiếm services của owner theo keyword
     * @param ownerUsername Username của owner
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách services
     */
    List<OwnerServiceResponse> searchServicesByOwner(String ownerUsername, String keyword);
    
    /**
     * Lấy thông tin service theo ID (chỉ nếu có items liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceId ID của service
     * @return Thông tin service
     */
    OwnerServiceResponse getServiceByIdAndOwner(String ownerUsername, Long serviceId);
    
    /**
     * Tạo service mới cho owner
     * @param ownerUsername Username của owner
     * @param request Thông tin service
     * @return Service đã tạo
     */
    OwnerServiceResponse createService(String ownerUsername, OwnerServiceRequest request);
    
    /**
     * Cập nhật service (chỉ nếu có items liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceId ID của service
     * @param request Thông tin cập nhật
     * @return Service đã cập nhật
     */
    OwnerServiceResponse updateService(String ownerUsername, Long serviceId, OwnerServiceRequest request);
    
    /**
     * Xóa service (chỉ nếu có items liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceId ID của service
     */
    void deleteService(String ownerUsername, Long serviceId);
    
    /**
     * Lấy danh sách service items của owner (có liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @return Danh sách service items
     */
    List<OwnerServiceItemResponse> getAllServiceItemsByOwner(String ownerUsername);
    
    /**
     * Lấy service item theo ID (chỉ nếu có liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceItemId ID của service item
     * @return Thông tin service item
     */
    OwnerServiceItemResponse getServiceItemByIdAndOwner(String ownerUsername, Long serviceItemId);
    
    /**
     * Tạo service item mới và liên kết với field của owner
     * @param ownerUsername Username của owner
     * @param request Thông tin service item và field
     * @return Service item đã tạo
     */
    OwnerServiceItemResponse createServiceItem(String ownerUsername, OwnerServiceItemRequest request);
    
    /**
     * Cập nhật service item (chỉ nếu có liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceItemId ID của service item
     * @param request Thông tin cập nhật
     * @return Service item đã cập nhật
     */
    OwnerServiceItemResponse updateServiceItem(String ownerUsername, Long serviceItemId, OwnerServiceItemRequest request);
    
    /**
     * Xóa service item (chỉ nếu có liên kết với fields của owner)
     * @param ownerUsername Username của owner
     * @param serviceItemId ID của service item
     */
    void deleteServiceItem(String ownerUsername, Long serviceItemId);
    
    /**
     * Lấy danh sách FieldServiceItems của một field cụ thể
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @return Danh sách FieldServiceItems
     */
    List<com.fisport.dto.response.OwnerFieldServiceResponse> getFieldServiceItemsByField(String ownerUsername, Long fieldId);
    
    /**
     * Thêm service item vào field với giá và số lượng
     * @param ownerUsername Username của owner
     * @param fieldId ID của field
     * @param serviceItemId ID của service item
     * @param price Giá bán tại sân
     * @param quantity Số lượng tồn kho
     */
    void addServiceItemToField(String ownerUsername, Long fieldId, Long serviceItemId, java.math.BigDecimal price, Integer quantity);
    
    /**
     * Xóa field service item khỏi field
     * @param ownerUsername Username của owner
     * @param fieldServiceItemId ID của field service item
     */
    void deleteFieldServiceItem(String ownerUsername, Long fieldServiceItemId);
}

