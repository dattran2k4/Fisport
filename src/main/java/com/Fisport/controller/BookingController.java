package com.Fisport.controller;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.*;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.BookingService;
import com.Fisport.service.FieldService;
import com.Fisport.service.FieldServiceItemService;
import com.Fisport.service.SubFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookingController {

    private final FieldService fieldService;
    private final BookingService bookingService;
    private final SubFieldService subFieldService;
    private final FieldServiceItemService fieldServiceItemService;

    @GetMapping("{slug}/dat-san")
    public String showBookingPage(Model model, @PathVariable String slug) {
        FieldDetailResponse field = fieldService.findBySlug(slug);
        model.addAttribute("field", field);

        List<SubFieldResponse> subField = subFieldService.getAllSubFields(field.getId(), ESubFieldStatus.AVAILABLE);
        model.addAttribute("subField", subField);

        List<FieldServiceItemResponse> items = fieldServiceItemService.getAllByActive(field.getId());
        model.addAttribute("items", items);

        model.addAttribute("booking", new BookingRequest());

        return "web/booking";
    }

    @PostMapping("/booking")
    public String booking(@ModelAttribute("booking") BookingRequest request,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                          Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
        }

        bookingService.createBooking(request, customUserDetails.getUser().getId());
        return "redirect:/payment";
    }
}
