package com.Fisport.controller.owner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/news")
public class OwnerNewsController {

    @GetMapping
    public String listNews(Model model) {
        model.addAttribute("content", "owner/dashboard :: content");
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createNewsForm(Model model) {
        model.addAttribute("content", "owner/news/create :: content");
        return "layout/owner/owner";
    }
}
