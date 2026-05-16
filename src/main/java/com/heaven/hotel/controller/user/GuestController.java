package com.heaven.hotel.controller.user;

import com.heaven.hotel.service.user.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guests")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping
    public String listGuests(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("pageTitle", "Guest Management");
        return "user/guests";
    }
}
