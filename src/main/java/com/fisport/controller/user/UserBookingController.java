package com.fisport.controller.user;

import com.fisport.common.ELevel;
import com.fisport.dto.request.ReviewRequest;
import com.fisport.service.BookingService;
import com.fisport.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/bookings")
@Slf4j(topic = "USER-BOOKING-CONTROLLER")
public class UserBookingController {

    private final BookingService bookingService;
    private final ReviewService reviewService;


    @GetMapping()
    public String showBookingsHistoryPage(Model model, Principal principal) {
        model.addAttribute("bookings", bookingService.getBookingsForUser(principal.getName()));
        model.addAttribute("levels", ELevel.values());
        return "user/booking";
    }

    @GetMapping("/review/{id}")
    public String showFormReview(@Min(1) @PathVariable("id") Long id, Model model, Principal principal) {
        model.addAttribute("booking", bookingService.getBookingForUser(id, principal.getName()));
        model.addAttribute("review", new ReviewRequest());
        return "user/review-form";
    }

    @PostMapping("/review/{id}")
    public String submitFormReview(@Min(1) @PathVariable("id") Long id,
                                   @Valid @ModelAttribute("review") ReviewRequest request,
                                   Principal principal, RedirectAttributes redirectAttributes, BindingResult result) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/user/bookings/review/" + id;
        }

        reviewService.addReview(request, principal.getName(), id);
        redirectAttributes.addFlashAttribute("review", "Cảm ơn bạn đã review sân!");
        return  "redirect:/user/bookings";
    }
}
