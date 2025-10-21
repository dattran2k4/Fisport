package com.Fisport.controller;

import com.Fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final FieldTypeService fieldTypeService;

    @ModelAttribute
    public void addFieldType(Model model) {
        Map<String, Object> common = new HashMap<>();
        common.put("fieldTypes", fieldTypeService.findAll());
        model.addAllAttributes(common);
    }


}
