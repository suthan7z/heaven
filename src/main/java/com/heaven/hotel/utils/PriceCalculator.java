package com.heaven.hotel.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for price calculations and financial computations
 */
public class PriceCalculator {
    
    private static final BigDecimal TAX_RATE = 
        BigDecimal.valueOf(Constants.DEFAULT_TAX_RATE);
    
    private PriceCalculator() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Calculate total price including tax
     */
    public static BigDecimal calculateWithTax(BigDecimal basePrice) {
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal tax = basePrice.multiply(TAX_RATE);
        return basePrice.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate tax amount
     */
    public static BigDecimal calculateTax(BigDecimal basePrice) {
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return basePrice.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate total price for multiple nights
     */
    public static BigDecimal calculateTotalForNights(BigDecimal pricePerNight, long nights) {
        if (pricePerNight == null || pricePerNight.compareTo(BigDecimal.ZERO) <= 0 || nights <= 0) {
            return BigDecimal.ZERO;
        }
        return pricePerNight.multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate total price for nights including tax
     */
    public static BigDecimal calculateTotalWithTaxForNights(BigDecimal pricePerNight, long nights) {
        BigDecimal total = calculateTotalForNights(pricePerNight, nights);
        return calculateWithTax(total);
    }
    
    /**
     * Calculate discount amount
     */
    public static BigDecimal calculateDiscount(BigDecimal originalPrice, double discountPercentage) {
        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) <= 0 || 
            discountPercentage < 0 || discountPercentage > 100) {
            return BigDecimal.ZERO;
        }
        BigDecimal discountRate = BigDecimal.valueOf(discountPercentage / 100);
        return originalPrice.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Apply discount to price
     */
    public static BigDecimal applyDiscount(BigDecimal price, double discountPercentage) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = calculateDiscount(price, discountPercentage);
        return price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Round to 2 decimal places
     */
    public static BigDecimal round(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Add two prices
     */
    public static BigDecimal add(BigDecimal price1, BigDecimal price2) {
        if (price1 == null) price1 = BigDecimal.ZERO;
        if (price2 == null) price2 = BigDecimal.ZERO;
        return price1.add(price2).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Subtract one price from another
     */
    public static BigDecimal subtract(BigDecimal price1, BigDecimal price2) {
        if (price1 == null) price1 = BigDecimal.ZERO;
        if (price2 == null) price2 = BigDecimal.ZERO;
        return price1.subtract(price2).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Check if price is valid (greater than zero)
     */
    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Calculate loyalty points based on price
     */
    public static long calculateLoyaltyPoints(BigDecimal price) {
        if (!isValidPrice(price)) {
            return 0;
        }
        // 1 point per dollar spent
        return price.longValue();
    }
}
