package com.Fisport.controller;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.service.AuthService;
import com.Fisport.service.impl.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@Controller
@RequestMapping()
public class LoginController {

    private final AuthService authService;
    private final SessionService sessionService;
    private final com.Fisport.service.UserService userService;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("login", new LoginRequestDTO());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("login") LoginRequestDTO loginRequestDTO,
                          @RequestParam(required = false) String backLink,
                          BindingResult result,
                          RedirectAttributes redirect,
                          Model model) {

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "login";
        }

        try {
            LoginResponse response = authService.login(loginRequestDTO);
            if (response.is2FAEnabled()) {
                return String.format("redirect:/2fa/verify?backLink=%s", backLink);
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Username hoặc password không đúng");
            return "login";
        }

        // determine redirect based on role (use username to avoid timing issues with SecurityContext)
        String target = determineRedirect(backLink, loginRequestDTO.getUsername());
        return "redirect:" + target;
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
                            @RequestParam(required = false) String backLink,
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

        String target = determineRedirect(backLink, twoFARequest.getUsername());
        return "redirect:" + target;
    }

    private String determineRedirect(String backLink, String username) {
        if (backLink != null && !backLink.isBlank()) return backLink;

        try {
            var user = userService.getUserByUserName(username);
            if (user != null && user.getRoleName() != null) {
                String role = user.getRoleName();
                if ("ADMIN".equalsIgnoreCase(role)) return "/admin";
                if ("OWNER".equalsIgnoreCase(role)) return "/owner";
            }
        } catch (Exception ignored) {
            // fallback to security context if userService fails
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                for (GrantedAuthority g : auth.getAuthorities()) {
                    String role = g.getAuthority();
                    if ("ROLE_ADMIN".equals(role) || "ADMIN".equals(role)) return "/admin";
                    if ("ROLE_OWNER".equals(role) || "OWNER".equals(role)) return "/owner";
                }
            }
        }

        return "/";
    }
}
