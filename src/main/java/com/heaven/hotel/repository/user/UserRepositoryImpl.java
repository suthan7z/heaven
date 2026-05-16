package com.heaven.hotel.repository.user;

import com.heaven.hotel.config.FilePathConfig;
import com.heaven.hotel.filehandler.DataParser;
import com.heaven.hotel.filehandler.FileReaderUtil;
import com.heaven.hotel.filehandler.FileWriterUtil;
import com.heaven.hotel.model.user.*;
import com.heaven.hotel.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of UserRepository
 * Uses pipe-delimited text files for persistence
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    @Autowired
    private FilePathConfig filePathConfig;
    
    private static final String FIELD_DELIMITER = "|";
    
    public UserRepositoryImpl() {
    }
    
    public UserRepositoryImpl(FilePathConfig filePathConfig) {
        this.filePathConfig = filePathConfig;
    }
    
    @Override
    public boolean save(User user) {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            String record = userToRecord(user);
            return FileWriterUtil.addRecord(filePath, record);
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean update(User user) {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            String record = userToRecord(user);
            return FileWriterUtil.updateRecord(filePath, user.getUserId(), record);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean delete(String userId) {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            return FileWriterUtil.deleteRecord(filePath, userId);
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<User> findById(String userId) {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            String record = FileReaderUtil.readRecordById(filePath, userId);
            if (record != null && !record.isEmpty()) {
                return Optional.of(recordToUser(record));
            }
        } catch (Exception e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        try {
            List<User> users = findAll();
            return users.stream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(username))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        try {
            List<User> users = findAll();
            return users.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public List<User> findAll() {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            List<String> records = FileReaderUtil.readAllRecords(filePath);
            return records.stream()
                    .filter(r -> r != null && !r.isBlank())
                    .map(r -> {
                        try { return recordToUser(r); }
                        catch (Exception e) {
                            System.err.println("Skipping bad user record: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding all users: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<User> findByRole(String role) {
        try {
            return findAll().stream()
                    .filter(u -> u.getRole().equalsIgnoreCase(role))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding users by role: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<User> findByStatus(String status) {
        try {
            return findAll().stream()
                    .filter(u -> u.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding users by status: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<User> findActiveUsers() {
        return findByStatus("ACTIVE");
    }
    
    @Override
    public List<User> findSuspendedUsers() {
        return findByStatus("SUSPENDED");
    }
    
    @Override
    public List<User> searchByName(String searchTerm) {
        try {
            String lowerTerm = searchTerm.toLowerCase();
            return findAll().stream()
                    .filter(u -> u.getFirstName().toLowerCase().contains(lowerTerm) ||
                               u.getLastName().toLowerCase().contains(lowerTerm) ||
                               u.getFullName().toLowerCase().contains(lowerTerm))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error searching users by name: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<User> searchByPhone(String phone) {
        try {
            return findAll().stream()
                    .filter(u -> u.getPhone() != null && u.getPhone().contains(phone))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error searching users by phone: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    @Override
    public int getTotalCount() {
        return findAll().size();
    }
    
    @Override
    public int getCountByRole(String role) {
        return (int) findAll().stream()
                .filter(u -> u.getRole().equalsIgnoreCase(role))
                .count();
    }
    
    @Override
    public boolean suspendUser(String userId) {
        Optional<User> optUser = findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setStatus("SUSPENDED");
            user.setUpdatedAt(LocalDateTime.now());
            return update(user);
        }
        return false;
    }
    
    @Override
    public boolean activateUser(String userId) {
        Optional<User> optUser = findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setStatus("ACTIVE");
            user.setUpdatedAt(LocalDateTime.now());
            return update(user);
        }
        return false;
    }
    
    @Override
    public boolean updateLastLogin(String userId) {
        Optional<User> optUser = findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setLastLogin(LocalDateTime.now());
            return update(user);
        }
        return false;
    }
    
    @Override
    public boolean deleteAll() {
        try {
            String filePath = filePathConfig.getUsersFilePath();
            return FileWriterUtil.clearFile(filePath);
        } catch (Exception e) {
            System.err.println("Error clearing users: " + e.getMessage());
            return false;
        }
    }
    
    // Helper methods
    
    /**
     * Convert User object to delimited string record
     * Format: userId|username|email|phone|firstName|lastName|passwordHash|role|status|createdAt|lastLogin|updatedAt|userType|extraData
     */
    private String userToRecord(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getUserId()).append(FIELD_DELIMITER);
        sb.append(user.getUsername()).append(FIELD_DELIMITER);
        sb.append(user.getEmail()).append(FIELD_DELIMITER);
        sb.append(user.getPhone()).append(FIELD_DELIMITER);
        sb.append(user.getFirstName()).append(FIELD_DELIMITER);
        sb.append(user.getLastName()).append(FIELD_DELIMITER);
        sb.append(user.getPasswordHash()).append(FIELD_DELIMITER);
        sb.append(user.getRole()).append(FIELD_DELIMITER);
        sb.append(user.getStatus()).append(FIELD_DELIMITER);
        sb.append(DateUtil.formatDateTime(user.getCreatedAt())).append(FIELD_DELIMITER);
        sb.append(user.getLastLogin() != null ? DateUtil.formatDateTime(user.getLastLogin()) : "").append(FIELD_DELIMITER);
        sb.append(DateUtil.formatDateTime(user.getUpdatedAt())).append(FIELD_DELIMITER);
        
        // User type specific fields
        if (user instanceof GuestUser) {
            GuestUser guest = (GuestUser) user;
            sb.append("GUEST").append(FIELD_DELIMITER);
            sb.append(guest.getAddress() != null ? guest.getAddress() : "").append(FIELD_DELIMITER);
            sb.append(guest.getCity() != null ? guest.getCity() : "").append(FIELD_DELIMITER);
            sb.append(guest.getCountry() != null ? guest.getCountry() : "").append(FIELD_DELIMITER);
            sb.append(guest.getPostalCode() != null ? guest.getPostalCode() : "").append(FIELD_DELIMITER);
            sb.append(guest.getLoyaltyTier()).append(FIELD_DELIMITER);
            sb.append(guest.getLoyaltyPoints());
        } else if (user instanceof StaffUser) {
            StaffUser staff = (StaffUser) user;
            sb.append("STAFF").append(FIELD_DELIMITER);
            sb.append(staff.getDepartment() != null ? staff.getDepartment() : "").append(FIELD_DELIMITER);
            sb.append(staff.getPosition() != null ? staff.getPosition() : "").append(FIELD_DELIMITER);
            sb.append(staff.getShift() != null ? staff.getShift() : "").append(FIELD_DELIMITER);
            sb.append(staff.getManagerId() != null ? staff.getManagerId() : "").append(FIELD_DELIMITER);
            sb.append(staff.getSalary()).append(FIELD_DELIMITER);
            sb.append(staff.getEmploymentStatus());
        } else if (user instanceof AdminUser) {
            AdminUser admin = (AdminUser) user;
            sb.append("ADMIN").append(FIELD_DELIMITER);
            sb.append(admin.getAdminLevel()).append(FIELD_DELIMITER);
            sb.append(admin.getDepartment() != null ? admin.getDepartment() : "").append(FIELD_DELIMITER);
            sb.append(admin.canManageAdmins()).append(FIELD_DELIMITER);
            sb.append(admin.canManageStaff()).append(FIELD_DELIMITER);
            sb.append(admin.canManageConfig()).append(FIELD_DELIMITER);
            sb.append(admin.canViewReports());
        }
        
        return sb.toString();
    }
    
    /**
     * Convert delimited string record to User object
     */
    private User recordToUser(String record) {
        String[] fields = DataParser.parseRecord(record);
        
        if (fields.length < 13) {
            return null;
        }
        
        String userId = fields[0];
        String username = fields[1];
        String email = fields[2];
        String phone = fields[3];
        String firstName = fields[4];
        String lastName = fields[5];
        String passwordHash = fields[6];
        String role = fields[7];
        String status = fields[8];
        String createdAt = fields[9];
        String lastLogin = fields[10];
        String updatedAt = fields[11];
        String userType = fields[12];
        
        User user = null;
        
        switch (userType) {
            case "GUEST":
                GuestUser guest = new GuestUser(username, email, phone, firstName, lastName);
                if (fields.length > 13) guest.setAddress(fields[13]);
                if (fields.length > 14) guest.setCity(fields[14]);
                if (fields.length > 15) guest.setCountry(fields[15]);
                if (fields.length > 16) guest.setPostalCode(fields[16]);
                if (fields.length > 17) guest.setLoyaltyTier(fields[17]);
                if (fields.length > 18) guest.setLoyaltyPoints(Long.parseLong(fields[18]));
                user = guest;
                break;
                
            case "STAFF":
                StaffUser staff = new StaffUser(username, email, phone, firstName, lastName, "", "");
                if (fields.length > 13) staff.setDepartment(fields[13]);
                if (fields.length > 14) staff.setPosition(fields[14]);
                if (fields.length > 15) staff.setShift(fields[15]);
                if (fields.length > 16) staff.setManagerId(fields[16]);
                if (fields.length > 17) staff.setSalary(Double.parseDouble(fields[17]));
                if (fields.length > 18) staff.setEmploymentStatus(fields[18]);
                user = staff;
                break;
                
            case "ADMIN":
                AdminUser admin = new AdminUser(username, email, phone, firstName, lastName);
                if (fields.length > 13) admin.setAdminLevel(fields[13]);
                if (fields.length > 14) admin.setDepartment(fields[14]);
                if (fields.length > 15) admin.setCanManageAdmins(Boolean.parseBoolean(fields[15]));
                if (fields.length > 16) admin.setCanManageStaff(Boolean.parseBoolean(fields[16]));
                if (fields.length > 17) admin.setCanManageConfig(Boolean.parseBoolean(fields[17]));
                if (fields.length > 18) admin.setCanViewReports(Boolean.parseBoolean(fields[18]));
                user = admin;
                break;
        }
        
        if (user != null) {
            user.setUserId(userId);
            user.setPasswordHash(passwordHash);
            user.setRole(role);
            user.setStatus(status);
            user.setCreatedAt(DateUtil.parseDateTime(createdAt));
            user.setLastLogin(lastLogin.isEmpty() ? null : DateUtil.parseDateTime(lastLogin));
            user.setUpdatedAt(DateUtil.parseDateTime(updatedAt));
        }
        
        return user;
    }
}
