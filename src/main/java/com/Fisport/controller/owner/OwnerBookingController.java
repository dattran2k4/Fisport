package com.Fisport.controller.owner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/bookings")
public class OwnerBookingController {
    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("content", "owner/bookings/list :: content");
        return "layout/owner/owner";
    }
}
