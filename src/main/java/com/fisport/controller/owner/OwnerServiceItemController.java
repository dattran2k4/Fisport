package com.fisport.controller.owner;

import com.fisport.dto.request.ServiceItemRequest;
import com.fisport.dto.response.ServiceItemResponse;
import com.fisport.dto.response.ServiceResponse;
import com.fisport.service.ServiceItemService;
import com.fisport.service.ServiceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/owner/services/{serviceId}/items")
@RequiredArgsConstructor
public class OwnerServiceItemController {

    private final ServiceItemService serviceItemService;
    private final ServiceService serviceService;

    @GetMapping
    public String listServiceItems(@PathVariable Long serviceId, Model model, HttpServletRequest request) {
        // Lấy thông tin Service
        ServiceResponse service = serviceService.getServiceById(serviceId);
        
        // Lấy danh sách ServiceItem thuộc Service này
        List<ServiceItemResponse> serviceItems = serviceItemService.findAllByServiceId(serviceId);
        
        model.addAttribute("service", service);
        model.addAttribute("serviceItems", serviceItems);
        model.addAttribute("content", "owner/services/items/list");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createServiceItemForm(@PathVariable Long serviceId, Model model, HttpServletRequest request) {
        // Lấy thông tin Service
        ServiceResponse service = serviceService.getServiceById(serviceId);
        
        ServiceItemRequest serviceItemRequest = new ServiceItemRequest();
        serviceItemRequest.setServiceId(serviceId);
        
        model.addAttribute("service", service);
        model.addAttribute("serviceItemRequest", serviceItemRequest);
        model.addAttribute("content", "owner/services/items/create");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @PostMapping("/create")
    public String createServiceItem(@PathVariable Long serviceId,
                                    @RequestParam("name") String name,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    HttpServletRequest request) {
        try {
            if (name == null || name.trim().isEmpty()) {
                ServiceResponse service = serviceService.getServiceById(serviceId);
                model.addAttribute("service", service);
                model.addAttribute("errorMessage", "Tên sản phẩm không được để trống");
                model.addAttribute("content", "owner/services/items/create");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }
            
            ServiceItemRequest request2 = new ServiceItemRequest();
            request2.setName(name.trim());
            request2.setServiceId(serviceId);
            
            serviceItemService.createServiceItem(request2);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo sản phẩm thành công!");
            return "redirect:/owner/services/" + serviceId + "/items";
        } catch (Exception e) {
            ServiceResponse service = serviceService.getServiceById(serviceId);
            model.addAttribute("service", service);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("content", "owner/services/items/create");
            model.addAttribute("currentUri", request.getRequestURI());
            return "layout/owner/owner";
        }
    }

    @GetMapping("/edit/{itemId}")
    public String editServiceItemForm(@PathVariable Long serviceId,
                                     @PathVariable Long itemId,
                                     Model model,
                                     HttpServletRequest request) {
        ServiceResponse service = serviceService.getServiceById(serviceId);
        ServiceItemResponse serviceItem = serviceItemService.findById(itemId);
        
        ServiceItemRequest serviceItemRequest = new ServiceItemRequest();
        serviceItemRequest.setName(serviceItem.getName());
        serviceItemRequest.setServiceId(serviceId);
        
        model.addAttribute("service", service);
        model.addAttribute("serviceItemRequest", serviceItemRequest);
        model.addAttribute("itemId", itemId);
        model.addAttribute("content", "owner/services/items/edit");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @PostMapping("/edit/{itemId}")
    public String updateServiceItem(@PathVariable Long serviceId,
                                   @PathVariable Long itemId,
                                   @RequestParam("name") String name,
                                   RedirectAttributes redirectAttributes,
                                   Model model,
                                   HttpServletRequest request) {
        try {
            if (name == null || name.trim().isEmpty()) {
                ServiceResponse service = serviceService.getServiceById(serviceId);
                model.addAttribute("service", service);
                model.addAttribute("itemId", itemId);
                model.addAttribute("errorMessage", "Tên sản phẩm không được để trống");
                model.addAttribute("content", "owner/services/items/edit");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }
            
            ServiceItemRequest request2 = new ServiceItemRequest();
            request2.setName(name.trim());
            request2.setServiceId(serviceId);
            
            serviceItemService.updateServiceItem(itemId, request2);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
            return "redirect:/owner/services/" + serviceId + "/items";
        } catch (Exception e) {
            ServiceResponse service = serviceService.getServiceById(serviceId);
            model.addAttribute("service", service);
            model.addAttribute("itemId", itemId);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("content", "owner/services/items/edit");
            model.addAttribute("currentUri", request.getRequestURI());
            return "layout/owner/owner";
        }
    }

    @PostMapping("/delete/{itemId}")
    public String deleteServiceItem(@PathVariable Long serviceId,
                                   @PathVariable Long itemId,
                                   RedirectAttributes redirectAttributes) {
        try {
            serviceItemService.deleteServiceItem(itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/owner/services/" + serviceId + "/items";
    }
}

