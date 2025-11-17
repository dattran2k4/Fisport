package com.fisport.controller;

import com.fisport.dto.response.*;
import com.fisport.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping
@Slf4j(topic = "HOME-CONTROLLER")
public class HomeController {

    private final CityService cityService;
    private final VoucherService voucherService;


    @GetMapping("/")
    public String showHomePage(Model model) {
        List<CityResponse> cities = cityService.findAll();
        List<VoucherResponse> vouchers = voucherService.findAllByActive();
        List<String> descriptions = vouchers.stream().map(VoucherResponse::getDescription).toList();
        model.addAttribute("cities", cities);
        model.addAttribute("vouchers", descriptions);
        return "web/index";
    }

}
