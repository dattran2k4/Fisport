package com.fisport.controller;

import com.fisport.dto.response.*;
import com.fisport.service.FeatureService;
import com.fisport.service.FieldService;
import com.fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/san/{slug}")
@Controller
@Slf4j(topic = "FIELD-CONTROLLER")
public class FieldController {

    private final FieldService fieldService;
    private final FeatureService featureService;

    @GetMapping()
    public String showFieldsPage(Model model,
                            @PathVariable String slug) {
        log.info("Show fields page by slug : {}", slug);

        List<FeatureResponse> featureResponses = featureService.getListFeatures();

        model.addAttribute("features", featureResponses);

        return "web/fields";
    }

    @GetMapping("/{fieldNameSlug}")
    public String showFieldDetailPage(@PathVariable String slug, @PathVariable String fieldNameSlug, Model model) {
        FieldDetailResponse fieldResponse = fieldService.findBySlug(fieldNameSlug);
        model.addAttribute("field", fieldResponse);
        return "web/field-detail";
    }
}
