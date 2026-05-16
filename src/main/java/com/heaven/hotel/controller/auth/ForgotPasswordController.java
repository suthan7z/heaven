package com.heaven.hotel.controller.auth;

import com.heaven.hotel.exception.UserNotFoundException;
import com.heaven.hotel.service.user.UserService;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for password reset (forgot password flow)
 */
@Controller
@RequestMapping
public class ForgotPasswordController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Display forgot password page
     */
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "auth/forgot-password";
    }
    
    /**
     * Handle forgot password request
     * In a real application, this would send a reset email with a token
     */
    @PostMapping("/auth/forgot-password")
    public String handleForgotPassword(
            @RequestParam String email,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (ValidationUtil.isEmpty(email)) {
            model.addAttribute("errorMessage", "Email is required");
            model.addAttribute("pageTitle", "Forgot Password");
            return "auth/forgot-password";
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            model.addAttribute("errorMessage", "Invalid email format");
            model.addAttribute("pageTitle", "Forgot Password");
            return "auth/forgot-password";
        }
        
        try {
            userService.getUserByEmail(email);
            
            // In a real application, generate a reset token and send email
            // For now, just confirm the email was found
            redirectAttributes.addFlashAttribute("successMessage", 
                "If an account exists with that email, you will receive password reset instructions.");
            return "redirect:/login";
            
        } catch (UserNotFoundException e) {
            // Security: Don't reveal if email exists
            redirectAttributes.addFlashAttribute("successMessage", 
                "If an account exists with that email, you will receive password reset instructions.");
            return "redirect:/login";
        }
    }
    
    /**
     * Display reset password page
     * In a real app, the token would be validated and extracted from URL
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam(required = false) String token,
            Model model) {
        
        if (ValidationUtil.isEmpty(token)) {
            model.addAttribute("errorMessage", "Invalid or expired reset link");
            model.addAttribute("pageTitle", "Reset Password");
            return "auth/forgot-password";
        }
        
        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset Password");
        return "auth/reset-password";
    }
    
    /**
     * Handle reset password submission
     */
    @PostMapping("/auth/reset-password")
    public String handleResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match");
                model.addAttribute("token", token);
                model.addAttribute("pageTitle", "Reset Password");
                return "auth/reset-password";
            }
            
            // In a real app, validate token and get associated email
            // For now, just show success message
            redirectAttributes.addFlashAttribute("successMessage", 
                "Password has been reset successfully. Please log in with your new password.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to reset password: " + e.getMessage());
            model.addAttribute("token", token);
            model.addAttribute("pageTitle", "Reset Password");
            return "auth/reset-password";
        }
    }
}
