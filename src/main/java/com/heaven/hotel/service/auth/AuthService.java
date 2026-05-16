package com.heaven.hotel.service.auth;

import com.heaven.hotel.exception.UserNotFoundException;
import com.heaven.hotel.model.user.User;
import com.heaven.hotel.service.user.UserService;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for authentication operations
 */
@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordService passwordService;
    
    /**
     * Authenticate user with username and password
     */
    public User authenticate(String username, String password) throws IllegalArgumentException {
        
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            throw new IllegalArgumentException("Username and password are required");
        }
        
        try {
            User user = userService.getUserByUsername(username);
            
            // Check if user is active
            if (!user.isActive()) {
                throw new IllegalArgumentException("User account is suspended or inactive");
            }
            
            // Verify password
            if (!passwordService.verifyPassword(password, user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid username or password");
            }
            
            // Update last login
            userService.updateLastLogin(user.getUserId());
            
            return user;
            
        } catch (UserNotFoundException e) {
            // Don't reveal if username exists
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
    
    /**
     * Authenticate user with email and password
     */
    public User authenticateByEmail(String email, String password) throws IllegalArgumentException {
        
        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            throw new IllegalArgumentException("Email and password are required");
        }
        
        try {
            User user = userService.getUserByEmail(email);
            
            // Check if user is active
            if (!user.isActive()) {
                throw new IllegalArgumentException("User account is suspended or inactive");
            }
            
            // Verify password
            if (!passwordService.verifyPassword(password, user.getPasswordHash())) {
                throw new IllegalArgumentException("Invalid email or password");
            }
            
            // Update last login
            userService.updateLastLogin(user.getUserId());
            
            return user;
            
        } catch (UserNotFoundException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
    
    /**
     * Validate user session/credentials
     */
    public boolean validateUser(String userId) {
        try {
            User user = userService.getUserById(userId);
            return user.isActive();
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Check if user has specific role
     */
    public boolean hasRole(String userId, String role) {
        try {
            User user = userService.getUserById(userId);
            return user.getRole().equalsIgnoreCase(role);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Check if user has any of the specified roles
     */
    public boolean hasAnyRole(String userId, String... roles) {
        try {
            User user = userService.getUserById(userId);
            for (String role : roles) {
                if (user.getRole().equalsIgnoreCase(role)) {
                    return true;
                }
            }
            return false;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Check if user has permission
     */
    public boolean hasPermission(String userId, String permissionCode) {
        try {
            User user = userService.getUserById(userId);
            return user.hasPermission(permissionCode);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Verify email exists
     */
    public boolean emailExists(String email) {
        try {
            userService.getUserByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Verify username exists
     */
    public boolean usernameExists(String username) {
        try {
            userService.getUserByUsername(username);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Reset password (for forgot password feature)
     */
    public void resetPassword(String email, String newPassword) throws IllegalArgumentException, UserNotFoundException {
        
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!passwordService.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        
        User user = userService.getUserByEmail(email);
        
        // Hash and update password
        user.setPasswordHash(passwordService.hashPassword(newPassword));
        if (!userService.getAllUsers().contains(user)) {
            // Update logic will be handled in userRepository
        }
    }
}
