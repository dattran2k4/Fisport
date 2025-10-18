package com.Fisport.controller.owner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/tournaments")
public class OwnerTournamentController {

    @GetMapping
    public String listTournaments(Model model) {
        model.addAttribute("content", "owner/tournaments/list :: content");
        return "layout/owner/owner";
    }

    @GetMapping("/create")
    public String createTournamentForm(Model model) {
        model.addAttribute("content", "owner/tournaments/create :: content");
        return "layout/owner/owner";
    }
}
