package com.fisport.controller;

import com.fisport.common.EFieldStatus;
import com.fisport.dto.response.*;
import com.fisport.service.FeatureService;
import com.fisport.service.FieldService;
import com.fisport.service.FieldTypeService;
import com.fisport.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/san/{slug}")
@Controller
public class FieldController {

    private final FieldService fieldService;
    private final FieldTypeService fieldTypeService;
    private final FeatureService featureService;
    private final WardService wardService;

    @GetMapping()
    public String getFields(Model model, @PathVariable String slug, @RequestParam(required = false) String ward,
                            @RequestParam(required = false) Long... featureIds) {
        List<FeatureResponse> featureResponses = featureService.getListFeatures();

        FieldTypeResponse fieldTypeResponse = fieldTypeService.findBySlug(slug);

        WardResponse wardResponse = (ward != null) ? wardService.getWardBySlug(ward) : null;
        Long wardId = wardResponse != null ? wardResponse.getId() : null;

        List<FieldResponse> responses = fieldService.getAllFields(wardId, fieldTypeResponse.getId(), EFieldStatus.ACTIVE, null, featureIds);

        model.addAttribute("fields", responses);
        model.addAttribute("features", featureResponses);
        return "web/fields";
    }

    @GetMapping("/{fieldNameSlug}")
    public String getField(@PathVariable String slug, @PathVariable String fieldNameSlug, Model model) {
        FieldTypeResponse fieldTypeResponse = fieldTypeService.findBySlug(slug);
        FieldDetailResponse fieldResponse = fieldService.findBySlug(fieldNameSlug);
        model.addAttribute("field", fieldResponse);
        return "web/field-detail";
    }
}
