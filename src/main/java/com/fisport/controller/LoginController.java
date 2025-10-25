package com.fisport.controller;

import com.fisport.dto.request.LoginRequestDTO;
import com.fisport.dto.request.TwoFARequest;
import com.fisport.dto.response.LoginResponse;
import com.fisport.service.AuthService;
import com.fisport.service.impl.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping()
public class LoginController {

    private final AuthService authService;
    private final SessionService sessionService;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("login", new LoginRequestDTO());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("login") LoginRequestDTO loginRequestDTO,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "login";
        }

        try {
            LoginResponse response = authService.login(loginRequestDTO);
            if (response.is2FAEnabled()) {
                return "redirect:/2fa/verify";
            }
            else if (response.getRole().equals("ADMIN")) {
                return "redirect:/admin";
            } else if (response.getRole().equals("OWNER")) {
                return "redirect:/owner";
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Username hoặc password không đúng");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/2fa/verify")
    public String show2faVerifyPage(Model model) {
        String username = sessionService.get("PRE_AUTH_USER", String.class);
        model.addAttribute("username", username);
        model.addAttribute("request", new TwoFARequest(username, null));

        return "2fa";
    }

    @PostMapping("/2fa/verify")
    public String verify2Fa(@Valid @ModelAttribute("request") TwoFARequest twoFARequest,
                            BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "2fa";
        }

        if (!authService.verify2FA(twoFARequest)) {
            model.addAttribute("username", sessionService.get("PRE_AUTH_USER", String.class));
            model.addAttribute("errorCode", "Mã xác thực không đúng hoặc phiên đã hết hạn");
            return "2fa";
        }

        String role = authService.getRoleByUserName(twoFARequest.getUsername());


        if (role.equals("ADMIN")) {
            return "redirect:/admin";
        }  else if (role.equals("OWNER")) {
            return "redirect:/owner";
        }

        return "redirect:/";
    }
}
