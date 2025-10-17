package com.Fisport.controller.user;

import com.Fisport.dto.response.BookingForUserResponse;
import com.Fisport.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/bookings")
public class OrdersController {

    private final BookingService bookingService;

    @GetMapping()
    public String bookings(Model model, Principal principal) {

        model.addAttribute("bookings", bookingService.getBookingsForUser(principal.getName()));
        return "user/booking";
    }
}
