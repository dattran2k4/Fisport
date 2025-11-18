package com.fisport.controller.owner;

import com.fisport.common.EFieldServiceItem;
import com.fisport.dto.response.FieldResponse;
import com.fisport.dto.response.OwnerServiceItemWithStatusResponse;
import com.fisport.dto.response.ServiceItemResponse;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Field;
import com.fisport.model.FieldServiceItem;
import com.fisport.model.ServiceItem;
import com.fisport.repository.FieldRepository;
import com.fisport.repository.FieldServiceItemRepository;
import com.fisport.repository.ServiceItemRepository;
import com.fisport.service.FieldService;
import com.fisport.service.ServiceItemService;
import com.fisport.service.ServiceService;
import com.fisport.dto.response.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller cho quản lý dịch vụ/sản phẩm của Owner
 * Nghiệp vụ: Owner chỉ được chọn và áp dụng service_item có sẵn vào field của họ
 * Flow:
 * 1. GET /owner/services → danh sách các field (sân) của owner
 * 2. GET /owner/services/{fieldId}/items → danh sách service_item với trạng thái attached/detached
 * 3. POST /owner/services/{fieldId}/items/add/{serviceItemId} → thêm service_item vào field
 * 4. POST /owner/services/{fieldId}/items/remove/{serviceItemId} → xóa service_item khỏi field
 */
@Controller
@RequestMapping("/owner/services")
@RequiredArgsConstructor
public class OwnerServiceController {

    private static final String CONTENT_INDEX = "owner/services/index";
    private static final String CONTENT_ITEMS = "owner/services/items";

    private final FieldService fieldService;
    private final ServiceItemService serviceItemService;
    private final ServiceService serviceService;
    private final FieldServiceItemRepository fieldServiceItemRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final FieldRepository fieldRepository;

    /**
     * GET /owner/services
     * Hiển thị danh sách các sân mà Owner sở hữu
     */
    @GetMapping(value = "", produces = "text/html")
    public String listFields(Model model,
                             HttpServletRequest request,
                             Principal principal) {
        try {
            List<FieldResponse> fields = fieldService.getMyOwnerFields(principal.getName());
            addCommonAttributes(model, request, CONTENT_INDEX);
            model.addAttribute("fields", fields);
        } catch (Exception e) {
            handleError(model, request, "Lỗi khi tải danh sách sân: " + e.getMessage(), CONTENT_INDEX);
            model.addAttribute("fields", List.of());
        }
        return "layout/owner/owner";
    }

    /**
     * Redirect từ route cũ /owner/services/field/{fieldId} sang route mới
     */
    @GetMapping("/field/{fieldId}")
    public String redirectOldRoute(@PathVariable Long fieldId) {
        return "redirect:/owner/services/" + fieldId + "/items";
    }

    /**
     * GET /owner/services/{fieldId}/items
     * Hiển thị danh sách service_item với trạng thái attached/detached
     */
    @GetMapping("/{fieldId}/items")
    public String listServiceItems(@PathVariable Long fieldId,
                                   Model model,
                                   HttpServletRequest request,
                                   Principal principal) {
        try {
            // Validate field ownership
            Field field = validateFieldOwnership(principal.getName(), fieldId);
            FieldResponse fieldResponse = fieldService.getField(fieldId);

            // Lấy tất cả service items từ database
            List<ServiceItemResponse> allServiceItems = serviceItemService.findAll();

            // Lấy danh sách FieldServiceItem đã được áp dụng vào field này
            List<FieldServiceItem> attachedItems = fieldServiceItemRepository.findByFieldId(fieldId);

            // Tạo Map để dễ tìm kiếm: key = serviceItemId, value = FieldServiceItem
            Map<Long, FieldServiceItem> attachedMap = new HashMap<>();
            for (FieldServiceItem fsi : attachedItems) {
                Long serviceItemId = fsi.getServiceItem().getId();
                attachedMap.put(serviceItemId, fsi);
            }

            // Tạo danh sách service items với trạng thái attached/detached
            List<OwnerServiceItemWithStatusResponse> itemsWithStatus = new ArrayList<>();
            for (ServiceItemResponse item : allServiceItems) {
                // Kiểm tra service item này đã được thêm vào field chưa
                Long itemId = item.getId();
                FieldServiceItem fieldServiceItem = attachedMap.get(itemId);
                boolean isAttached = (fieldServiceItem != null);

                // Lấy giá và số lượng nếu đã attached
                BigDecimal price = null;
                Integer quantity = null;
                Long fieldServiceItemId = null;
                String status = null;

                if (isAttached) {
                    if (fieldServiceItem.getPrice() != null) {
                        price = fieldServiceItem.getPrice();
                    }
                    if (fieldServiceItem.getQuantity() != null) {
                        quantity = fieldServiceItem.getQuantity();
                    }
                    fieldServiceItemId = fieldServiceItem.getId();
                    if (fieldServiceItem.getStatus() != null) {
                        status = fieldServiceItem.getStatus().name();
                    }
                }

                // Lấy tên service item
                String serviceItemName = "";
                if (item.getName() != null) {
                    serviceItemName = item.getName();
                }

                // Lấy tên service
                String serviceName = "";
                if (item.getServiceName() != null) {
                    serviceName = item.getServiceName();
                }

                // Tạo response object
                OwnerServiceItemWithStatusResponse response = OwnerServiceItemWithStatusResponse.builder()
                        .serviceItemId(itemId)
                        .serviceItemName(serviceItemName)
                        .serviceId(item.getService_id())
                        .serviceName(serviceName)
                        .isAttached(isAttached)
                        .fieldServiceItemId(fieldServiceItemId)
                        .price(price)
                        .quantity(quantity)
                        .status(status)
                        .build();

                itemsWithStatus.add(response);
            }

            // Lấy danh sách tất cả services để hiển thị trong filter
            List<ServiceResponse> allServices = serviceService.getAllServices();

            addCommonAttributes(model, request, CONTENT_ITEMS);
            model.addAttribute("field", fieldResponse);
            model.addAttribute("items", itemsWithStatus);
            model.addAttribute("services", allServices);
        } catch (Exception e) {
            handleError(model, request, e.getMessage(), CONTENT_INDEX);
        }
        return "layout/owner/owner";
    }

    /**
     * POST /owner/services/{fieldId}/items/add/{serviceItemId}
     * Thêm service_item vào field với giá và số lượng
     */
    @PostMapping("/{fieldId}/items/add/{serviceItemId}")
    public String addServiceItem(@PathVariable Long fieldId,
                                 @PathVariable Long serviceItemId,
                                 @RequestParam(value = "price", required = false) String priceStr,
                                 @RequestParam(value = "quantity", required = false) String quantityStr,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        try {
            // Validate field ownership
            Field field = validateFieldOwnership(principal.getName(), fieldId);

            // Kiểm tra service item có tồn tại không
            java.util.Optional<ServiceItem> serviceItemOptional = serviceItemRepository.findById(serviceItemId);
            if (!serviceItemOptional.isPresent()) {
                throw new ResourceNotFoundException("Không tìm thấy dịch vụ");
            }
            ServiceItem serviceItem = serviceItemOptional.get();

            // Kiểm tra đã tồn tại chưa
            java.util.Optional<FieldServiceItem> existingItem = fieldServiceItemRepository.findByFieldIdAndServiceItemId(fieldId, serviceItemId);
            if (existingItem.isPresent()) {
                throw new IllegalArgumentException("Dịch vụ này đã được thêm vào sân rồi");
            }

            // Parse price và quantity từ form
            BigDecimal price = parseBigDecimal(priceStr);
            Integer quantity = parseInteger(quantityStr);

            // Tạo và lưu FieldServiceItem mới
            FieldServiceItem fieldServiceItem = FieldServiceItem.builder()
                    .field(field)
                    .serviceItem(serviceItem)
                    .price(price)
                    .quantity(quantity)
                    .status(EFieldServiceItem.ACTIVE)
                    .build();

            fieldServiceItemRepository.save(fieldServiceItem);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm dịch vụ vào sân thành công!");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage() != null ? e.getMessage() : "Có lỗi xảy ra khi thêm dịch vụ");
        }
        return "redirect:/owner/services/" + fieldId + "/items";
    }

    /**
     * POST /owner/services/{fieldId}/items/remove/{serviceItemId}
     * Xóa service_item khỏi field
     */
    @PostMapping("/{fieldId}/items/remove/{serviceItemId}")
    public String removeServiceItem(@PathVariable Long fieldId,
                                    @PathVariable Long serviceItemId,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        try {
            // Validate field ownership
            Field field = validateFieldOwnership(principal.getName(), fieldId);

            // Tìm FieldServiceItem
            java.util.Optional<FieldServiceItem> fieldServiceItemOptional =
                    fieldServiceItemRepository.findByFieldIdAndServiceItemId(fieldId, serviceItemId);

            if (!fieldServiceItemOptional.isPresent()) {
                throw new ResourceNotFoundException("Không tìm thấy dịch vụ trong sân này");
            }
            FieldServiceItem fieldServiceItem = fieldServiceItemOptional.get();

            // Xóa FieldServiceItem
            fieldServiceItemRepository.delete(fieldServiceItem);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa dịch vụ khỏi sân thành công!");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage() != null ? e.getMessage() : "Có lỗi xảy ra khi xóa dịch vụ");
        }
        return "redirect:/owner/services/" + fieldId + "/items";
    }

    // ========== Helper Methods ==========

    /**
     * Kiểm tra field có thuộc về owner không
     * @throws ResponseStatusException HTTP 403 nếu không có quyền
     */
    private Field validateFieldOwnership(String ownerUsername, Long fieldId) {
        // Lấy danh sách field của owner
        List<FieldResponse> ownerFields = fieldService.getMyOwnerFields(ownerUsername);

        // Tìm field có ID trùng với fieldId
        FieldResponse fieldResponse = null;
        for (FieldResponse field : ownerFields) {
            if (field.getId().equals(fieldId)) {
                fieldResponse = field;
                break;
            }
        }

        // Nếu không tìm thấy, throw exception
        if (fieldResponse == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Không tìm thấy sân hoặc bạn không có quyền truy cập"
            );
        }

        // Lấy Field entity từ database
        java.util.Optional<Field> fieldOptional = fieldRepository.findById(fieldId);
        if (fieldOptional.isPresent()) {
            return fieldOptional.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy sân");
        }
    }

    /**
     * Thêm các attributes chung
     */
    private void addCommonAttributes(Model model, HttpServletRequest request, String content) {
        model.addAttribute("content", content);
        model.addAttribute("currentUri", request.getRequestURI());
    }

    /**
     * Xử lý lỗi chung
     */
    private void handleError(Model model, HttpServletRequest request, String errorMessage, String content) {
        model.addAttribute("errorMessage", errorMessage);
        addCommonAttributes(model, request, content);
    }

    /**
     * Parse BigDecimal từ string, loại bỏ dấu phẩy và khoảng trắng
     */
    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String cleaned = value.trim().replaceAll("[,\\s]", "");
            return cleaned.isEmpty() ? BigDecimal.ZERO : new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Parse Integer từ string, loại bỏ dấu phẩy và khoảng trắng
     */
    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            String cleaned = value.trim().replaceAll("[,\\s]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
