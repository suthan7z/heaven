package com.heaven.hotel.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile(Constants.EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile(Constants.PHONE_REGEX);
    
    private ValidationUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && !phone.isBlank() && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        
        int length = password.length();
        if (length < Constants.MIN_PASSWORD_LENGTH || 
            length > Constants.MAX_PASSWORD_LENGTH) {
            return false;
        }
        
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/\\\\|`~].*");
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    
    /**
     * Validate name (not null, not blank, length between min and max)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        int length = name.length();
        return length >= Constants.MIN_NAME_LENGTH && 
               length <= Constants.MAX_NAME_LENGTH &&
               name.matches("^[a-zA-Z\\s'-]+$");
    }
    
    /**
     * Validate string is not null and not blank
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isBlank();
    }
    
    /**
     * Validate string is null or blank
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }
    
    /**
     * Validate integer is positive
     */
    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }
    
    /**
     * Validate long is positive
     */
    public static boolean isPositive(Long value) {
        return value != null && value > 0;
    }
    
    /**
     * Validate double is positive
     */
    public static boolean isPositive(Double value) {
        return value != null && value > 0;
    }
    
    /**
     * Validate integer is non-negative
     */
    public static boolean isNonNegative(Integer value) {
        return value != null && value >= 0;
    }
    
    /**
     * Validate value is within range
     */
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }
    
    /**
     * Validate date string format (yyyy-MM-dd)
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (isEmpty(dateStr)) {
            return false;
        }
        try {
            DateUtil.parseDate(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validate username (alphanumeric and underscore, 3-20 characters)
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * Sanitize user input (remove potential XSS characters)
     */
    public static String sanitizeInput(String input) {
        if (isEmpty(input)) {
            return "";
        }
        return input.replaceAll("[<>\"'&]", "");
    }
    
    /**
     * Validate room number format
     */
    public static boolean isValidRoomNumber(String roomNumber) {
        if (isEmpty(roomNumber)) {
            return false;
        }
        return roomNumber.matches("^[0-9]{1,4}$");
    }
    
    /**
     * Validate UUID format
     */
    public static boolean isValidUUID(String uuid) {
        if (isEmpty(uuid)) {
            return false;
        }
        try {
            java.util.UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
