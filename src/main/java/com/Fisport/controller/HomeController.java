package com.Fisport.controller;

import com.Fisport.common.EFieldStatus;
import com.Fisport.dto.response.*;
import com.Fisport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
@RequestMapping
public class HomeController {

    private final CityService cityService;
    private final FieldService fieldService;

    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // Không làm gì cả, chỉ để chặn request này
    }

    @GetMapping("/")
    public String home(Model model) {
        List<CityResponse> cities = cityService.findAll();
//        List<FieldResponse> fields = fieldService.getAllFields(null, null, EFieldStatus.ACTIVE, null, null);
        model.addAttribute("cities", cities);
//        model.addAttribute("fields", fields);

        return "web/index";
    }

}
