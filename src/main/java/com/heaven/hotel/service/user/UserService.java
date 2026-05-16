package com.heaven.hotel.service.user;

import com.heaven.hotel.exception.UserNotFoundException;
import com.heaven.hotel.model.user.*;
import com.heaven.hotel.repository.user.UserRepository;
import com.heaven.hotel.service.auth.PasswordService;
import com.heaven.hotel.utils.IdGenerator;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for user management operations
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    /**
     * Register a new guest user
     */
    public User registerGuestUser(String username, String email, String phone, 
                                   String firstName, String lastName, String password) 
            throws IllegalArgumentException {
        
        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username format");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone format");
        }
        if (!ValidationUtil.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!ValidationUtil.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!passwordService.isValidPassword(password)) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        
        // Check for duplicates
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new guest user
        GuestUser guestUser = new GuestUser(username, email, phone, firstName, lastName);
        guestUser.setUserId(IdGenerator.generateUserId());
        guestUser.setPasswordHash(passwordService.hashPassword(password));
        
        // Save to repository
        if (userRepository.save(guestUser)) {
            return guestUser;
        } else {
            throw new RuntimeException("Failed to register user");
        }
    }
    
    /**
     * Register a new staff user (admin only)
     */
    public User registerStaffUser(String username, String email, String phone,
                                   String firstName, String lastName, String password,
                                   String department, String position)
            throws IllegalArgumentException {
        
        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username format");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtil.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!ValidationUtil.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!passwordService.isValidPassword(password)) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        if (ValidationUtil.isEmpty(department) || ValidationUtil.isEmpty(position)) {
            throw new IllegalArgumentException("Department and position are required");
        }
        
        // Check for duplicates
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new staff user
        StaffUser staffUser = new StaffUser(username, email, phone, firstName, lastName, department, position);
        staffUser.setUserId(IdGenerator.generateUserId());
        staffUser.setPasswordHash(passwordService.hashPassword(password));
        
        // Save to repository
        if (userRepository.save(staffUser)) {
            return staffUser;
        } else {
            throw new RuntimeException("Failed to register staff user");
        }
    }
    
    /**
     * Register a new admin user (super admin only)
     */
    public User registerAdminUser(String username, String email, String phone,
                                   String firstName, String lastName, String password)
            throws IllegalArgumentException {
        
        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username format");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtil.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!ValidationUtil.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!passwordService.isValidPassword(password)) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        
        // Check for duplicates
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new admin user
        AdminUser adminUser = new AdminUser(username, email, phone, firstName, lastName);
        adminUser.setUserId(IdGenerator.generateUserId());
        adminUser.setPasswordHash(passwordService.hashPassword(password));
        adminUser.setCanManageStaff(true);
        adminUser.setCanManageConfig(true);
        adminUser.setCanViewReports(true);
        
        // Save to repository
        if (userRepository.save(adminUser)) {
            return adminUser;
        } else {
            throw new RuntimeException("Failed to register admin user");
        }
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
    
    /**
     * Get user by email
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
    
    /**
     * Update user profile
     */
    public User updateUserProfile(String userId, String phone, String firstName, String lastName)
            throws UserNotFoundException, IllegalArgumentException {
        
        User user = getUserById(userId);
        
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone format");
        }
        if (!ValidationUtil.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }
        if (!ValidationUtil.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }
        
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUpdatedAt(LocalDateTime.now());
        
        if (userRepository.update(user)) {
            return user;
        } else {
            throw new RuntimeException("Failed to update user profile");
        }
    }
    
    /**
     * Update guest user address
     */
    public GuestUser updateGuestAddress(String userId, String address, String city,
                                         String country, String postalCode)
            throws UserNotFoundException, IllegalArgumentException {
        
        User user = getUserById(userId);
        if (!(user instanceof GuestUser)) {
            throw new IllegalArgumentException("User is not a guest");
        }
        
        GuestUser guest = (GuestUser) user;
        guest.setAddress(address);
        guest.setCity(city);
        guest.setCountry(country);
        guest.setPostalCode(postalCode);
        guest.setUpdatedAt(LocalDateTime.now());
        
        if (userRepository.update(guest)) {
            return guest;
        } else {
            throw new RuntimeException("Failed to update guest address");
        }
    }
    
    /**
     * Change user password
     */
    public void changePassword(String userId, String currentPassword, String newPassword)
            throws UserNotFoundException, IllegalArgumentException {
        
        User user = getUserById(userId);
        
        // Verify current password
        if (!passwordService.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Validate new password
        if (!passwordService.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(passwordService.getPasswordRequirements());
        }
        
        // Check if new password is same as current
        if (passwordService.verifyPassword(newPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }
        
        // Update password
        user.setPasswordHash(passwordService.hashPassword(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        if (!userRepository.update(user)) {
            throw new RuntimeException("Failed to change password");
        }
    }
    
    /**
     * Suspend user account
     */
    public void suspendUser(String userId) throws UserNotFoundException {
        getUserById(userId); // Check if user exists
        if (!userRepository.suspendUser(userId)) {
            throw new RuntimeException("Failed to suspend user");
        }
    }
    
    /**
     * Activate user account
     */
    public void activateUser(String userId) throws UserNotFoundException {
        getUserById(userId); // Check if user exists
        if (!userRepository.activateUser(userId)) {
            throw new RuntimeException("Failed to activate user");
        }
    }
    
    /**
     * Delete user
     */
    public void deleteUser(String userId) throws UserNotFoundException {
        getUserById(userId); // Check if user exists
        if (!userRepository.delete(userId)) {
            throw new RuntimeException("Failed to delete user");
        }
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get users by role
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Get active users
     */
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    /**
     * Get suspended users
     */
    public List<User> getSuspendedUsers() {
        return userRepository.findSuspendedUsers();
    }
    
    /**
     * Search users by name
     */
    public List<User> searchUsersByName(String searchTerm) {
        if (ValidationUtil.isEmpty(searchTerm)) {
            return getAllUsers();
        }
        return userRepository.searchByName(searchTerm);
    }
    
    /**
     * Get total user count
     */
    public int getTotalUserCount() {
        return userRepository.getTotalCount();
    }
    
    /**
     * Get user count by role
     */
    public int getUserCountByRole(String role) {
        return userRepository.getCountByRole(role);
    }
    
    /**
     * Update last login
     */
    public void updateLastLogin(String userId) throws UserNotFoundException {
        getUserById(userId); // Check if user exists
        if (!userRepository.updateLastLogin(userId)) {
            throw new RuntimeException("Failed to update last login");
        }
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin(String userId) throws UserNotFoundException {
        return getUserById(userId).isAdmin();
    }
    
    /**
     * Check if user is staff
     */
    public boolean isStaff(String userId) throws UserNotFoundException {
        return getUserById(userId).isStaff();
    }
    
    /**
     * Check if user is guest
     */
    public boolean isGuest(String userId) throws UserNotFoundException {
        return getUserById(userId).isGuest();
    }
}
