package com.heaven.hotel.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs
 */
public class IdGenerator {
    
    private static final AtomicLong userIdCounter = new AtomicLong(1000);
    private static final AtomicLong bookingIdCounter = new AtomicLong(5000);
    private static final AtomicLong paymentIdCounter = new AtomicLong(8000);
    private static final AtomicLong roomIdCounter = new AtomicLong(100);
    private static final AtomicLong reviewIdCounter = new AtomicLong(3000);
    private static final AtomicLong keycardIdCounter = new AtomicLong(2000);
    
    private IdGenerator() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Generate unique user ID with prefix "USR"
     */
    public static String generateUserId() {
        return "USR" + userIdCounter.incrementAndGet();
    }
    
    /**
     * Generate unique booking ID with prefix "BKG"
     */
    public static String generateBookingId() {
        return "BKG" + bookingIdCounter.incrementAndGet();
    }
    
    /**
     * Generate unique payment ID with prefix "PAY"
     */
    public static String generatePaymentId() {
        return "PAY" + paymentIdCounter.incrementAndGet();
    }
    
    /**
     * Generate unique room ID with prefix "RM"
     */
    public static String generateRoomId() {
        return "RM" + roomIdCounter.incrementAndGet();
    }
    
    /**
     * Generate unique review ID with prefix "REV"
     */
    public static String generateReviewId() {
        return "REV" + reviewIdCounter.incrementAndGet();
    }
    
    /**
     * Generate unique keycard ID with prefix "KC"
     */
    public static String generateKeycardId() {
        return "KC" + keycardIdCounter.incrementAndGet();
    }
    
    /**
     * Generate random UUID string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate transaction reference number
     */
    public static String generateTransactionRef() {
        return "TXN" + System.currentTimeMillis();
    }
    
    /**
     * Generate unique ID with a custom prefix
     */
    public static String generateId(String prefix) {
        switch (prefix.toUpperCase()) {
            case "USR":
            case "USER":
                return generateUserId();
            case "BKG":
            case "BOOK":
                return generateBookingId();
            case "PAY":
            case "PAYMENT":
                return generatePaymentId();
            case "RM":
            case "ROOM":
                return generateRoomId();
            case "REV":
            case "REVIEW":
                return generateReviewId();
            case "KC":
            case "KEYCARD":
                return generateKeycardId();
            default:
                return prefix.toUpperCase() + UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
        }
    }
    
    /**
     * Generate session ID
     */
    public static String generateSessionId() {
        return "SES" + UUID.randomUUID().toString().substring(0, 12);
    }
}
