package com.Fisport.controller;

import com.Fisport.common.EFieldStatus;
import com.Fisport.dto.response.FeatureResponse;
import com.Fisport.dto.response.FieldResponse;
import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.dto.response.WardResponse;
import com.Fisport.service.FeatureService;
import com.Fisport.service.FieldService;
import com.Fisport.service.FieldTypeService;
import com.Fisport.service.WardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping
@Controller
public class FieldController {

    private final FieldService fieldService;
    private final FieldTypeService fieldTypeService;
    private final FeatureService featureService;
    private final WardService wardService;

    @GetMapping("/{slug}")
    public String getFields(Model model, @PathVariable String slug, @RequestParam(required = false) String ward,
                            @RequestParam(required = false) Long... featureIds) {
        List<FeatureResponse> featureResponses = featureService.getListFeatures();

        FieldTypeResponse fieldTypeResponse = fieldTypeService.findBySlug(slug);

        if (fieldTypeResponse == null) {
            return "redirect:/not-found?Field type not found";
        }

        WardResponse wardResponse = (ward != null) ? wardService.getWardBySlug(ward) : null;
        Long wardId = wardResponse != null ? wardResponse.getId() : null;

        List<FieldResponse> responses = fieldService.getAllFields(wardId, fieldTypeResponse.getId(), EFieldStatus.ACTIVE, null, null, featureIds);

        model.addAttribute("fields", responses);
        model.addAttribute("features", featureResponses);
        return "web/fields";
    }

    @GetMapping("/{slug}/{fieldNameSlug}")
    public String getField(@PathVariable String slug, @PathVariable String fieldNameSlug, Model model) {
        FieldTypeResponse fieldTypeResponse = fieldTypeService.findBySlug(slug);
        FieldResponse fieldResponse = fieldService.findBySlug(fieldNameSlug);
        model.addAttribute("field", fieldResponse);
        return "web/field-detail";
    }
}
