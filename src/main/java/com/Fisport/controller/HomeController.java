package com.Fisport.controller;

import com.Fisport.dto.response.FieldTypeResponse;
import com.Fisport.service.FieldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
public class HomeController {

    private final FieldTypeService fieldTypeService;

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/home");
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<FieldTypeResponse> list = fieldTypeService.findAll();
        model.addAttribute("fieldTypes", list);
        return "web/index";
    }

    @GetMapping("/fields")
    public String fields(Model model) {
        List<FieldTypeResponse> list = fieldTypeService.findAll();
        model.addAttribute("fieldTypes", list);
        return "web/fields";
    }

}
