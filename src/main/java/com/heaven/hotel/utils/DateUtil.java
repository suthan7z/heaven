package com.heaven.hotel.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date/time operations
 */
public class DateUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern(Constants.TIME_PATTERN);
    
    private DateUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Format LocalDate to string using default pattern
     */
    public static String formatDate(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string using default pattern
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_TIME_FORMATTER);
    }
    
    /**
     * Format time to string using default pattern
     */
    public static String formatTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(TIME_FORMATTER);
    }
    
    /**
     * Parse string to LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        return dateStr == null ? null : LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    
    /**
     * Parse string to LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr == null ? null : LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }
    
    /**
     * Get current date
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
    
    /**
     * Get current date and time
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
    
    /**
     * Calculate number of nights between two dates
     */
    public static long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * Check if date is between two dates (inclusive)
     */
    public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return (date.isEqual(start) || date.isAfter(start)) && 
               (date.isEqual(end) || date.isBefore(end));
    }
    
    /**
     * Add days to a date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date == null ? null : date.plusDays(days);
    }
    
    /**
     * Subtract days from a date
     */
    public static LocalDate subtractDays(LocalDate date, long days) {
        return date == null ? null : date.minusDays(days);
    }
}
