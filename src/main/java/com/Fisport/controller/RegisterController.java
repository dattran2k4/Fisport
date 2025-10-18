package com.Fisport.controller;

import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.request.TwoFARequest;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.service.AuthService;
import com.Fisport.service.TwoFAService;
import com.Fisport.service.UserService;
import com.Fisport.service.impl.SessionService;
import com.Fisport.util.QRCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Validated
@RequiredArgsConstructor
@RequestMapping
@Controller
public class RegisterController {
    private final AuthService authService;
    private final SessionService sessionService;
    private final QRCodeUtil qrCodeUtil;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterRequestDTO());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("register") @Validated RegisterRequestDTO request,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "/register";
        }

        RegisterResponseDTO response = authService.register(request);
        redirectAttributes.addFlashAttribute("success",
                "Đăng ký tài khoản thành công! Vui lòng xác thực email để xác thực tài khoản.");
        sessionService.set("username", response.getUsername());
        return "redirect:/register";
    }

    @GetMapping("/confirm")
    public String showConfirmPage(@NotBlank @RequestParam String verifyCode, Model model) {
        String qrUrl = authService.confirmUser(verifyCode);
        String qrCode = qrCodeUtil.generateQRCodeBase64(qrUrl, 250, 250);
        String username = sessionService.get("username", String.class);

        //to-do the message
        if (qrUrl != null) {
            model.addAttribute("success", "Xác nhận email thành công, thực hiện bước cuối để hoàn thành kích hoạt tài khoản");
            model.addAttribute("qrCode", qrCode);
            model.addAttribute("request", new TwoFARequest(username, null));
            return "2fa-register";
        }
        
        return "2fa-register";
    }

    @PostMapping("/2fa-register")
    public String TwoFARegister(@ModelAttribute("request") TwoFARequest request, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "/confirm";
        }

        authService.verify2FARegister(request.getUsername(), request.getCode());
        sessionService.remove("username");
        redirectAttributes.addFlashAttribute("TwoFASuccess", "Xác thực 2FA thành công, đăng nhập lại để hiểu vấn đề");
        return "redirect:/login";
    }

}
