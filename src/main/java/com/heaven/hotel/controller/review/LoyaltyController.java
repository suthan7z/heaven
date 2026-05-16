package com.heaven.hotel.controller.review;

import com.heaven.hotel.service.review.LoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loyalty")
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping
    public String loyaltyPage(Model model, Authentication auth) {
        if (auth != null) {
            loyaltyService.findByGuestId(auth.getName())
                    .ifPresent(lp -> model.addAttribute("loyalty", lp));
        }
        model.addAttribute("pageTitle", "Loyalty Program");
        return "review/loyalty";
    }
}
