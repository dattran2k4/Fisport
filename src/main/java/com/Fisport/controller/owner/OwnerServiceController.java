package com.Fisport.controller.owner;

import com.Fisport.dto.request.ServiceRequest;
import com.Fisport.dto.response.ServiceResponse;
import com.Fisport.service.OwnerFieldServiceService;
import com.Fisport.service.ServiceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/owner/services")
@RequiredArgsConstructor
public class OwnerServiceController {
    private final ServiceService serviceService;
    private final OwnerFieldServiceService ownerFieldServiceService;

    @GetMapping
    public String listServices(@RequestParam(value = "q", required = false) String q,
                               Model model, HttpServletRequest request) {
        List<ServiceResponse> services = (q == null || q.trim().isEmpty())
                ? serviceService.getAllServices()
                : serviceService.searchServices(q);
        int totalItems = services.stream().mapToInt(s -> s.getServiceItems() != null ? s.getServiceItems().size() : 0).sum();
        double averageItems = services.size() > 0 ? (double) totalItems / services.size() : 0.0;
        model.addAttribute("services", services);
        model.addAttribute("q", q);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("averageItems", averageItems);
        model.addAttribute("content", "owner/services/list");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createServiceForm(Model model) {
        model.addAttribute("serviceRequest", new ServiceRequest());
        model.addAttribute("content", "owner/services/create");
        return "layout/owner/owner";
    }

    @PostMapping("/create")
    public String createService(@RequestParam("name") String name, RedirectAttributes redirectAttributes, Model model) {
        try {
            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Tên dịch vụ không được để trống");
                model.addAttribute("content", "owner/services/create");
                return "layout/owner/owner";
            }
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setName(name.trim());
            serviceService.createService(serviceRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo dịch vụ thành công!");
            return "redirect:/owner/services";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("content", "owner/services/create");
            return "layout/owner/owner";
        }
    }

    @GetMapping("/edit/{id}")
    public String editServiceForm(@PathVariable Long id, Model model) {
        ServiceResponse service = serviceService.getServiceById(id);
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setName(service.getName());
        model.addAttribute("serviceRequest", serviceRequest);
        model.addAttribute("serviceId", id);
        model.addAttribute("content", "owner/services/edit");
        return "layout/owner/owner";
    }

    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable Long id, @RequestParam("name") String name, RedirectAttributes redirectAttributes, Model model) {
        try {
            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Tên dịch vụ không được để trống");
                model.addAttribute("serviceId", id);
                model.addAttribute("content", "owner/services/edit");
                return "layout/owner/owner";
            }
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setName(name.trim());
            serviceService.updateService(id, serviceRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật dịch vụ thành công!");
            return "redirect:/owner/services";
        } catch (Exception e) {
            model.addAttribute("serviceId", id);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("content", "owner/services/edit");
            return "layout/owner/owner";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            serviceService.deleteService(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa dịch vụ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/owner/services";
    }
}