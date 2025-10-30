package com.fisport.controller.user;

import com.fisport.dto.request.UpdateProfileRequest;
import com.fisport.dto.response.UserResponse;
import com.fisport.service.UserService;
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
@RequestMapping("/user/profile")
public class ProfileController {

    private final UserService userService;

    @GetMapping()
    public String profile(Model model, Principal principal) {
        UserResponse user = userService.getUserByUserName(principal.getName());
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setBirthday(user.getBirthday());
        request.setGender(user.getGender());
        request.setPhone(user.getPhone());
        request.setEmail(user.getEmail());
        model.addAttribute("profile", request);
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("profile") UpdateProfileRequest request,  Model model, Principal principal,
                                BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute(result.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("profile", request); // giữ dữ liệu đã nhập
            return "user/profile"; // hiển thị lỗi ngay trên form
        }

        userService.updateUserByUserName(request, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "redirect:/user/profile";
    }

}
