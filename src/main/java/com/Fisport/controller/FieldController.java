package com.Fisport.controller;

import com.Fisport.common.EFieldStatus;
import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.service.FeatureService;
import com.Fisport.service.FieldService;
import com.Fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping
@Controller
public class FieldController {

    private final FieldService fieldService;
    private final FieldTypeService fieldTypeService;
    private final FeatureService featureService;

    @GetMapping("/{slug}")
    public String getFields(@PathVariable String slug, Model model) {
        List<FeatureResponse> featureResponses = featureService.getListFeatures();
        FieldTypeResponse fieldTypeResponse = fieldTypeService.findBySlug(slug);
        List<FieldResponse> responses = fieldService.getAllFields(null, fieldTypeResponse.getId(), EFieldStatus.ACTIVE, null, null, null);
        model.addAttribute("fields", responses);
        model.addAttribute("features", featureResponses);
        return "web/fields.html";
    }

    @GetMapping("/{slug}/{fieldNameSlug}")
    public String getField(@PathVariable String slug, @PathVariable String fieldNameSlug, Model model) {

        return "web/field-detail.html";
    }
}
