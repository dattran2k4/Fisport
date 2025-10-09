package com.Fisport.controller;

import com.Fisport.common.EFieldStatus;
import com.Fisport.dto.response.*;
import com.Fisport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
public class HomeController {

    private final FieldTypeService fieldTypeService;
    private final CityService cityService;
    private final FieldService fieldService;

//    @GetMapping("/")
//    public RedirectView root() {
//        return new RedirectView("/home");
//    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // Không làm gì cả, chỉ để chặn request này
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<FieldTypeResponse> types = fieldTypeService.findAll();
        List<CityResponse> cities = cityService.findAll();

        List<FieldResponse> fields = fieldService.getAllFields(null, null, EFieldStatus.ACTIVE, null, null, null);

        model.addAttribute("fieldTypes", types);
        model.addAttribute("cities", cities);
        model.addAttribute("fields", fields);

        return "web/index";
    }

}
