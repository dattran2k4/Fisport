package com.fisport.controller;

import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.request.BookingRequest;
import com.fisport.dto.response.*;
import com.fisport.security.CustomUserDetails;
import com.fisport.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j(topic = "BOOKING-CONTROLLER")
public class BookingController {

    private final FieldService fieldService;
    private final BookingService bookingService;
    private final SubFieldService subFieldService;
    private final FieldServiceItemService fieldServiceItemService;
    private final VoucherService voucherService;

    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/san/{fieldTypeSlug}/{fieldNameSlug}/dat-san")
    public String showBookingPage(Model model, @PathVariable String fieldTypeSlug, @PathVariable String fieldNameSlug, Principal principal) {

        FieldDetailResponse field = fieldService.findBySlug(fieldNameSlug);
        model.addAttribute("field", field);

        List<SubFieldResponse> subField = subFieldService.getAllSubFields(field.getId(), ESubFieldStatus.AVAILABLE);
        model.addAttribute("subField", subField);

        List<FieldServiceItemResponse> items = fieldServiceItemService.getAllByActive(field.getId());
        model.addAttribute("items", items);

        if (!model.containsAttribute("booking")) {
            model.addAttribute("booking", new BookingRequest());
        }

        model.addAttribute("vouchers", voucherService.getVouchersByUserId(principal.getName()));

        return "web/booking";
    }

    @PostMapping("/san/{fieldTypeSlug}/{fieldNameSlug}/dat-san")
    public String booking(@Valid @ModelAttribute("booking") BookingRequest request,
                          BindingResult result,
                          @PathVariable String fieldTypeSlug,
                          @PathVariable String fieldNameSlug,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                          Model model) {

        FieldDetailResponse field = fieldService.findBySlug(fieldNameSlug);
        model.addAttribute("field", field);

        List<SubFieldResponse> subField = subFieldService.getAllSubFields(field.getId(), ESubFieldStatus.AVAILABLE);
        model.addAttribute("subField", subField);

        List<FieldServiceItemResponse> items = fieldServiceItemService.getAllByActive(field.getId());
        model.addAttribute("items", items);

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "web/booking";
        }
        String token = null;
        try {
            token = bookingService.createBooking(request, customUserDetails.getUser().getId());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "web/booking";
        }
        return "redirect:/thanh-toan?token=" + token;
    }
}
