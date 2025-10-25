package com.fisport.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        model.addAttribute("status", statusCode);
        model.addAttribute("path", requestUri);

        switch (statusCode != null ? statusCode : 500) {
            case 404:
                model.addAttribute("message", "Page or resource not found");
                break;
            case 500:
                model.addAttribute("message", "Internal server error");
                break;
            default:
                model.addAttribute("message", "Unexpected error");
                break;
        }

        return "error";
    }

    @RequestMapping("/access-denied")
    public String showPageAccessDenied() {
        return "access-denied";
    }
}
