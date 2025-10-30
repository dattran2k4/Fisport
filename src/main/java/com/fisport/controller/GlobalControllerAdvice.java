package com.fisport.controller;

import com.fisport.service.FieldTypeService;
import jakarta.servlet.http.HttpServletRequest;

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
    public void addFieldType(Model model, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        
        // Không load fieldTypes cho owner routes và API routes
        if (requestURI.startsWith("/owner") || requestURI.startsWith("/api")) {
            return;
        }
        
        Map<String, Object> common = new HashMap<>();
        common.put("fieldTypes", fieldTypeService.findAll());
        model.addAllAttributes(common);
    }


}
