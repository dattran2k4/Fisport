package com.Fisport.controller;

import com.Fisport.common.ESubFieldStatus;
import com.Fisport.dto.request.BookingRequest;
import com.Fisport.dto.response.BookingServiceItemResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldServiceItemResponse;
import com.Fisport.dto.response.SubFieldResponse;
import com.Fisport.service.BookingService;
import com.Fisport.service.FieldService;
import com.Fisport.service.FieldServiceItemService;
import com.Fisport.service.SubFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String datSan(Model model, @PathVariable String slug) {
        FieldResponse field = fieldService.findBySlug(slug);
        model.addAttribute("field", field);

        List<SubFieldResponse> subField = subFieldService.getAllSubFields(field.getId(), ESubFieldStatus.AVAILABLE);
        model.addAttribute("subField", subField);

        List<FieldServiceItemResponse> items = fieldServiceItemService.getAllByActive(field.getId());
        model.addAttribute("items", items);

        model.addAttribute("bookingRequest", new BookingRequest());

        return "web/booking";
    }
}
