package com.Fisport.controller;

import com.Fisport.dto.response.CityResponse;
import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.dto.response.WardResponse;
import com.Fisport.service.CityService;
import com.Fisport.service.FeatureService;
import com.Fisport.service.FieldTypeService;
import com.Fisport.service.WardService;
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

        model.addAttribute("fieldTypes", types);
        model.addAttribute("cities", cities);

        return "web/index";
    }

//    @GetMapping("/fields")
//    public String fields(Model model) {
//        List<FieldTypeResponse> list = fieldTypeService.findAll();
//        model.addAttribute("fieldTypes", list);
//        return "web/fields";
//    }

}
