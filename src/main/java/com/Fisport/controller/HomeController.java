package com.Fisport.controller;

import com.Fisport.dto.response.*;
import com.Fisport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
@RequestMapping
public class HomeController {

    private final CityService cityService;
    private final VoucherService voucherService;
    private final FieldTypeService fieldTypeService;


    @GetMapping("/")
    public String home(Model model) {
        List<CityResponse> cities = cityService.findAll();
        List<VoucherResponse> vouchers = voucherService.findAllByActive();
        List<String> descriptions = vouchers.stream().map(VoucherResponse::getDescription).toList();
        List<FieldTypeResponse> fieldTypes = fieldTypeService.findAll();
        
        model.addAttribute("cities", cities);
        model.addAttribute("vouchers", descriptions);
        model.addAttribute("fieldTypes", fieldTypes);
        return "web/index";
    }

}
