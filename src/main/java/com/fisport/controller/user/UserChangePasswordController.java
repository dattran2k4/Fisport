package com.fisport.controller.user;

import com.fisport.dto.request.ChangePasswordRequest;
import com.fisport.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserChangePasswordController {

    private final UserService userService;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, Principal principal) {
        model.addAttribute("pass", new ChangePasswordRequest());
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("pass") ChangePasswordRequest request, Principal principal, RedirectAttributes redirectAttributes, BindingResult result) {
        //sh
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/user/change-password";
        }

        userService.changePasswordByUserName(request, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật mật khẩu thành công!");
        return  "redirect:/user/change-password";
    }
}
