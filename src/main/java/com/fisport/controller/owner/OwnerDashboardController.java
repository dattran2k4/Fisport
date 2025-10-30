package com.fisport.controller.owner;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/owner")
public class OwnerDashboardController {


    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model, HttpServletRequest request) {
        model.addAttribute("content", "dashboard");
        model.addAttribute("currentUri", request.getRequestURI());

        return "owner/dashboard";
    }


}
