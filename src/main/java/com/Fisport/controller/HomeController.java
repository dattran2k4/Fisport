package com.Fisport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/home");
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "web/index";
    }

}
