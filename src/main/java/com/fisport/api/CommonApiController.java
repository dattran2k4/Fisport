package com.fisport.api;

import com.fisport.dto.response.ResponseData;
import com.fisport.dto.response.ResponseError;
import com.fisport.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CommonApiController {
    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseData<?> sendEmail(@RequestParam String recipients, @RequestParam String subject,
                                     @RequestParam String content, @RequestParam(required = false) MultipartFile[] files) {
        try {
            return new ResponseData(HttpStatus.ACCEPTED.value(), mailService.sendEmail(recipients, subject, content, files));
        } catch (UnsupportedEncodingException | MessagingException e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Sending email was failure");
        }
    }
}
