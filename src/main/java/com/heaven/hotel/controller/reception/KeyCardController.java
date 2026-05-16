package com.heaven.hotel.controller.reception;

import com.heaven.hotel.service.reception.KeyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reception/keycards")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class KeyCardController {

    @Autowired
    private KeyCardService keyCardService;

    @GetMapping
    public String keyCards(Model model) {
        model.addAttribute("keyCards", keyCardService.getAllKeyCards());
        model.addAttribute("pageTitle", "Key Card Management");
        return "reception/keycards";
    }
}
