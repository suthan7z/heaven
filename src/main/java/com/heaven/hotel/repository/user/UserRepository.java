package com.heaven.hotel.repository.user;

import com.heaven.hotel.model.user.User;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for user persistence operations
 */
public interface UserRepository {
    
    /**
     * Save a new user
     */
    boolean save(User user);
    
    /**
     * Update an existing user
     */
    boolean update(User user);
    
    /**
     * Delete a user by ID
     */
    boolean delete(String userId);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(String userId);
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Get all users
     */
    List<User> findAll();
    
    /**
     * Get all users by role
     */
    List<User> findByRole(String role);
    
    /**
     * Get all users by status
     */
    List<User> findByStatus(String status);
    
    /**
     * Get active users
     */
    List<User> findActiveUsers();
    
    /**
     * Get suspended users
     */
    List<User> findSuspendedUsers();
    
    /**
     * Search users by name
     */
    List<User> searchByName(String searchTerm);
    
    /**
     * Search users by phone
     */
    List<User> searchByPhone(String phone);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Get total user count
     */
    int getTotalCount();
    
    /**
     * Get user count by role
     */
    int getCountByRole(String role);
    
    /**
     * Suspend user
     */
    boolean suspendUser(String userId);
    
    /**
     * Activate user
     */
    boolean activateUser(String userId);
    
    /**
     * Update last login time
     */
    boolean updateLastLogin(String userId);
    
    /**
     * Clear all users (for testing)
     */
    boolean deleteAll();
}
