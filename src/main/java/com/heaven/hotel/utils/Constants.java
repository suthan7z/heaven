package com.heaven.hotel.utils;

/**
 * Application-wide constants
 */
public class Constants {
    
    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STAFF = "STAFF";
    public static final String ROLE_GUEST = "GUEST";
    
    // User Status
    public static final String USER_STATUS_ACTIVE = "ACTIVE";
    public static final String USER_STATUS_SUSPENDED = "SUSPENDED";
    public static final String USER_STATUS_INACTIVE = "INACTIVE";
    
    // Room Status
    public static final String ROOM_STATUS_AVAILABLE = "AVAILABLE";
    public static final String ROOM_STATUS_OCCUPIED = "OCCUPIED";
    public static final String ROOM_STATUS_MAINTENANCE = "MAINTENANCE";
    public static final String ROOM_STATUS_RESERVED = "RESERVED";
    
    // Booking Status
    public static final String BOOKING_STATUS_PENDING = "PENDING";
    public static final String BOOKING_STATUS_CONFIRMED = "CONFIRMED";
    public static final String BOOKING_STATUS_CHECKED_IN = "CHECKED_IN";
    public static final String BOOKING_STATUS_CHECKED_OUT = "CHECKED_OUT";
    public static final String BOOKING_STATUS_CANCELLED = "CANCELLED";
    
    // Payment Status
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String PAYMENT_STATUS_FAILED = "FAILED";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";
    
    // Review Status
    public static final String REVIEW_STATUS_PENDING = "PENDING";
    public static final String REVIEW_STATUS_APPROVED = "APPROVED";
    public static final String REVIEW_STATUS_REJECTED = "REJECTED";
    
    // Date Formats
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";
    
    // Default Values
    public static final double DEFAULT_TAX_RATE = 0.10; // 10% tax
    public static final int DEFAULT_CHECKOUT_HOUR = 12;
    public static final int DEFAULT_CHECKIN_HOUR = 14;
    public static final int MAX_ROOM_OCCUPANCY = 4;
    public static final int MIN_ROOM_OCCUPANCY = 1;
    
    // File Paths
    public static final String DATA_DIR = "src/main/resources/data/";
    public static final String USERS_FILE = "users.txt";
    public static final String BOOKINGS_FILE = "bookings.txt";
    public static final String ROOMS_FILE = "rooms.txt";
    public static final String PAYMENTS_FILE = "payments.txt";
    public static final String REVIEWS_FILE = "reviews.txt";
    public static final String MAINTENANCE_FILE = "maintenance.txt";
    public static final String KEYCARDS_FILE = "keycards.txt";
    public static final String LOYALTY_FILE = "loyalty.txt";
    public static final String ADMIN_LOGS_FILE = "admin_logs.txt";
    public static final String HOTEL_CONFIG_FILE = "hotel_config.txt";
    
    // Delimiters for file parsing
    public static final String FILE_DELIMITER = "\\|";
    public static final String FIELD_DELIMITER = "|";
    
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_REGEX = "^[+]?[0-9]{7,15}$";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
