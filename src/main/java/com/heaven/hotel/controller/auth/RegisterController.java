package com.heaven.hotel.controller.auth;

import com.heaven.hotel.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for user registration (guest and staff)
 */
@Controller
@RequestMapping
public class RegisterController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Display guest registration page
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "Create Account");
        return "auth/register";
    }
    
    /**
     * Handle guest user registration
     */
    @PostMapping("/auth/register")
    public String registerGuest(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate passwords match
            if (!password.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match");
                model.addAttribute("pageTitle", "Create Account");
                return "auth/register";
            }
            
            // Register user
            userService.registerGuestUser(username, email, phone, firstName, lastName, password);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please log in with your credentials.");
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("pageTitle", "Create Account");
            return "auth/register";
        }
    }
    
    /**
     * Display success page after registration
     */
    @GetMapping("/register-success")
    public String registerSuccess(Model model) {
        model.addAttribute("pageTitle", "Registration Successful");
        return "auth/register-success";
    }
}
