package com.Fisport.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model, HttpServletRequest request) {
        model.addAttribute("content", "dashboard");
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("title", "Admin Dashboard - Quản trị hệ thống");

        return "admin/dashboard";
    }
}

