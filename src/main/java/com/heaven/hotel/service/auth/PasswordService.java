package com.heaven.hotel.service.auth;

import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling password operations (hashing, verification, validation)
 */
@Service
public class PasswordService {
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Hash a plain text password
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            return null;
        }
        return passwordEncoder.encode(plainPassword);
    }
    
    /**
     * Verify a plain text password against a hash
     */
    public boolean verifyPassword(String plainPassword, String passwordHash) {
        if (plainPassword == null || passwordHash == null) {
            return false;
        }
        return passwordEncoder.matches(plainPassword, passwordHash);
    }
    
    /**
     * Validate password strength
     */
    public boolean isValidPassword(String password) {
        return ValidationUtil.isValidPassword(password);
    }
    
    /**
     * Get password strength score (0-5)
     */
    public int getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        int strength = 0;
        
        // Length check
        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;
        
        // Character variety checks
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/\\\\|`~].*")) strength++;
        
        return Math.min(strength, 5);
    }
    
    /**
     * Get password strength label
     */
    public String getPasswordStrengthLabel(String password) {
        int strength = getPasswordStrength(password);
        switch (strength) {
            case 0:
            case 1:
                return "Weak";
            case 2:
                return "Fair";
            case 3:
                return "Good";
            case 4:
                return "Strong";
            case 5:
                return "Very Strong";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Check if password meets minimum requirements
     */
    public boolean meetsMinimumRequirements(String password) {
        return password != null && 
               password.length() >= 8 &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*\\d.*");
    }
    
    /**
     * Generate password requirements message
     */
    public String getPasswordRequirements() {
        return "Password must contain: " +
               "at least 8 characters, " +
               "one uppercase letter, " +
               "one lowercase letter, " +
               "one number, " +
               "and one special character.";
    }
}
