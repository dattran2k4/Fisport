package com.Fisport.controller.owner;

import com.Fisport.dto.request.VoucherRequest;
import com.Fisport.dto.response.VoucherResponse;
import com.Fisport.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/owner/vouchers")
@RequiredArgsConstructor
public class OwnerVoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public String listVouchers(Model model, HttpServletRequest request) {
        List<VoucherResponse> vouchers = voucherService.getAllVouchers();
        model.addAttribute("vouchers", vouchers);
        model.addAttribute("content", "owner/vouchers/list");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createVoucherForm(Model model, HttpServletRequest request) {
        model.addAttribute("voucherRequest", new VoucherRequest());
        model.addAttribute("content", "owner/vouchers/create");
        model.addAttribute("currentUri", request.getRequestURI());
        return "layout/owner/owner";
    }

    @PostMapping("/create")
    public String createVoucher(@RequestParam("code") String code,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam("discount") Integer discount,
                               @RequestParam("startDate") String startDate,
                               @RequestParam("endDate") String endDate,
                               @RequestParam(value = "limit", required = false) Integer limit,
                               RedirectAttributes redirectAttributes,
                               Model model,
                               HttpServletRequest request) {
        try {
            // Validation
            if (code == null || code.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Mã voucher không được để trống");
                model.addAttribute("content", "owner/vouchers/create");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            if (discount == null || discount < 1 || discount > 100) {
                model.addAttribute("errorMessage", "Giảm giá phải từ 1% đến 100%");
                model.addAttribute("content", "owner/vouchers/create");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end) || start.isEqual(end)) {
                model.addAttribute("errorMessage", "Ngày kết thúc phải sau ngày bắt đầu");
                model.addAttribute("content", "owner/vouchers/create");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            VoucherRequest voucherRequest = new VoucherRequest();
            voucherRequest.setCode(code.trim().toUpperCase());
            voucherRequest.setDescription(description != null ? description.trim() : null);
            voucherRequest.setDiscount(discount);
            voucherRequest.setStartDate(start);
            voucherRequest.setEndDate(end);
            voucherRequest.setLimit(limit);

            voucherService.createVoucher(voucherRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo voucher thành công!");
            return "redirect:/owner/vouchers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi: " + e.getMessage());
            model.addAttribute("content", "owner/vouchers/create");
            model.addAttribute("currentUri", request.getRequestURI());
            return "layout/owner/owner";
        }
    }

    @GetMapping("/edit/{id}")
    public String editVoucherForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            VoucherResponse voucher = voucherService.getVoucherById(id);
            
            VoucherRequest voucherRequest = new VoucherRequest();
            voucherRequest.setCode(voucher.getCode());
            voucherRequest.setDescription(voucher.getDescription());
            voucherRequest.setDiscount(voucher.getDiscount());
            voucherRequest.setStartDate(voucher.getStartDate());
            voucherRequest.setEndDate(voucher.getEndDate());
            voucherRequest.setLimit(voucher.getLimit());
            
            model.addAttribute("voucherRequest", voucherRequest);
            model.addAttribute("voucherId", id);
            model.addAttribute("content", "owner/vouchers/edit");
            model.addAttribute("currentUri", request.getRequestURI());
            return "layout/owner/owner";
        } catch (Exception e) {
            RedirectAttributes redirectAttributes = (RedirectAttributes) model.asMap().get("redirectAttributes");
            if (redirectAttributes != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy voucher");
            }
            return "redirect:/owner/vouchers";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateVoucher(@PathVariable Long id,
                               @RequestParam("code") String code,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam("discount") Integer discount,
                               @RequestParam("startDate") String startDate,
                               @RequestParam("endDate") String endDate,
                               @RequestParam(value = "limit", required = false) Integer limit,
                               RedirectAttributes redirectAttributes,
                               Model model,
                               HttpServletRequest request) {
        try {
            // Validation
            if (code == null || code.trim().isEmpty()) {
                model.addAttribute("errorMessage", "Mã voucher không được để trống");
                model.addAttribute("voucherId", id);
                model.addAttribute("content", "owner/vouchers/edit");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            if (discount == null || discount < 1 || discount > 100) {
                model.addAttribute("errorMessage", "Giảm giá phải từ 1% đến 100%");
                model.addAttribute("voucherId", id);
                model.addAttribute("content", "owner/vouchers/edit");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end) || start.isEqual(end)) {
                model.addAttribute("errorMessage", "Ngày kết thúc phải sau ngày bắt đầu");
                model.addAttribute("voucherId", id);
                model.addAttribute("content", "owner/vouchers/edit");
                model.addAttribute("currentUri", request.getRequestURI());
                return "layout/owner/owner";
            }

            VoucherRequest voucherRequest = new VoucherRequest();
            voucherRequest.setCode(code.trim().toUpperCase());
            voucherRequest.setDescription(description != null ? description.trim() : null);
            voucherRequest.setDiscount(discount);
            voucherRequest.setStartDate(start);
            voucherRequest.setEndDate(end);
            voucherRequest.setLimit(limit);

            voucherService.updateVoucher(id, voucherRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật voucher thành công!");
            return "redirect:/owner/vouchers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi: " + e.getMessage());
            model.addAttribute("voucherId", id);
            model.addAttribute("content", "owner/vouchers/edit");
            model.addAttribute("currentUri", request.getRequestURI());
            return "layout/owner/owner";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            voucherService.deleteVoucher(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa voucher thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/owner/vouchers";
    }
}
