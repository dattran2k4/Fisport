package com.fisport.controller.owner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/vouchers")
public class OwnerVoucherController {

    @GetMapping
    public String listVouchers(Model model) {
        model.addAttribute("content", "owner/vouchers/list :: content");
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createVoucherForm(Model model) {
        model.addAttribute("content", "owner/vouchers/create :: content");
        return "layout/owner/owner";
    }
}
