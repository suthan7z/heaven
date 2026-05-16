package com.heaven.hotel.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logoutPage(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "auth/logout";
    }
}
