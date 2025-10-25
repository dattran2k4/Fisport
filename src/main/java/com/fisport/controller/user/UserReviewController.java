package com.fisport.controller.user;

import com.fisport.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/reviews")
public class UserReviewController {

    private final ReviewService reviewService;

    @GetMapping()
    public String reviews(Model model, Principal principal) {
        model.addAttribute("reviews", reviewService.getReviewsByUser(principal.getName()));
        return "user/reviews";
    }
}

