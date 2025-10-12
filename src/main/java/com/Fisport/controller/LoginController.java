package com.Fisport.controller;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@Controller
@RequestMapping()
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("login", new LoginRequestDTO());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("login") LoginRequestDTO loginRequestDTO, BindingResult result, HttpSession session, RedirectAttributes redirect, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "login";
        }

        try {
            LoginResponse response = authService.login(loginRequestDTO, session);
            if (response.is2FAEnabled()) {
//                redirect.addFlashAttribute("response", response);
                return "redirect:/2fa/verify";
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Username hoặc password không đúng");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/2fa/verify")
    public String show2faVerifyPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("PRE_AUTH_USER");
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        TwoFARequest twoFARequest = new TwoFARequest();
        twoFARequest.setUsername(username);
        model.addAttribute("request", twoFARequest);

        return "2fa";
    }

    @PostMapping("/2fa/verify")
    public String verify2Fa(@Valid @ModelAttribute("request") TwoFARequest twoFARequest,
                            BindingResult result, HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "2fa";
        }

        if (!authService.verify2FA(twoFARequest, session)) {
            model.addAttribute("errorCode", "Mã xác thực không đúng hoặc phiên đã hết hạn");
            return "2fa";
        }

        return "redirect:/";
    }
}
