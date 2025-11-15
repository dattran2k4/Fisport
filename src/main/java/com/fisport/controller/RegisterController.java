package com.fisport.controller;

import com.fisport.dto.request.RegisterRequestDTO;
import com.fisport.dto.request.TwoFARequest;
import com.fisport.dto.response.RegisterResponseDTO;
import com.fisport.service.AuthService;
import com.fisport.service.impl.SessionService;
import com.fisport.util.QRCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j(topic = "REGISTER-CONTROLLER")
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
        log.info("Regisger request");

        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "/register";
        }

        try {
            RegisterResponseDTO response = authService.register(request);
            redirectAttributes.addFlashAttribute("success",
                    "Đăng ký tài khoản thành công! Vui lòng xác thực email để xác thực tài khoản.");
            sessionService.set("username", response.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            log.info("Error register {}", e.getMessage());
            return "/register";
        }
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

            sessionService.set("qrUrl", qrUrl);
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

        String qrUrl = sessionService.get("qrUrl", String.class);
        if (qrUrl != null) {
            // Tạo lại QR code để hiển thị nếu OTP nhập sai
            String qrCode = qrCodeUtil.generateQRCodeBase64(qrUrl, 250, 250);
            model.addAttribute("qrCode", qrCode);
        }

        try {
            authService.verify2FARegister(request.getUsername(), request.getCode());
            sessionService.remove("username");
            sessionService.remove("qrUrl");
            redirectAttributes.addFlashAttribute("TwoFASuccess", "Xác thực 2FA thành công, đăng nhập lại để hiểu vấn đề");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("request", request); // giữ thông tin username
            return "2fa-register"; // trả lại view 2FA để nhập lại
        }
    }

}
