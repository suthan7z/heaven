package com.heaven.hotel.model.user;

import java.time.LocalDateTime;

/**
 * Abstract base class for all user types
 */
public abstract class User {
    
    protected String userId;
    protected String username;
    protected String email;
    protected String phone;
    protected String firstName;
    protected String lastName;
    protected String passwordHash;
    protected String role;
    protected String status;
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLogin;
    protected LocalDateTime updatedAt;
    
    // Constructor
    public User() {
    }
    
    public User(String username, String email, String phone, String firstName, String lastName, String role) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Abstract methods that subclasses must implement
    public abstract String getDisplayName();
    
    public abstract String getDefaultDashboardPage();
    
    public abstract boolean hasPermission(String permissionCode);
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    public boolean isSuspended() {
        return "SUSPENDED".equals(status);
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isStaff() {
        return "STAFF".equals(role);
    }
    
    public boolean isGuest() {
        return "GUEST".equals(role);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
