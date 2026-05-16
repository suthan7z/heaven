package com.heaven.hotel.service.booking;

import com.heaven.hotel.model.room.Room;
import org.springframework.stereotype.Service;

/**
 * Service for price calculation and management
 */
@Service
public class PricingService {
    
    private static final double TAX_RATE = 0.10; // 10% tax
    private static final double WEEKEND_MULTIPLIER = 1.2; // 20% increase on weekends
    private static final double PEAK_SEASON_MULTIPLIER = 1.3; // 30% increase during peak season
    
    /**
     * Calculate base price for a room booking
     */
    public double calculateBasePrice(Room room, int numberOfNights) {
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Number of nights must be greater than 0");
        }
        
        double nightly_rate = room.getCalculatedPrice();
        double totalPrice = nightly_rate * numberOfNights;
        
        // Apply weekend multiplier if applicable (simplified logic)
        boolean hasWeekend = numberOfNights >= 2; // If multiple nights, assume at least one weekend
        if (hasWeekend) {
            totalPrice *= WEEKEND_MULTIPLIER;
        }
        
        return totalPrice;
    }
    
    /**
     * Calculate tax amount
     */
    public double calculateTax(double basePrice) {
        return basePrice * TAX_RATE;
    }
    
    /**
     * Calculate total price with tax
     */
    public double calculateTotalPrice(Room room, int numberOfNights) {
        double basePrice = calculateBasePrice(room, numberOfNights);
        double taxAmount = calculateTax(basePrice);
        return basePrice + taxAmount;
    }
    
    /**
     * Apply discount to price
     */
    public double applyDiscount(double originalPrice, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        return originalPrice * (1 - discountPercentage / 100);
    }
    
    /**
     * Apply early bird discount
     */
    public double applyEarlyBirdDiscount(double originalPrice, int daysInAdvance) {
        if (daysInAdvance >= 30) {
            return applyDiscount(originalPrice, 15); // 15% discount for 30+ days advance
        } else if (daysInAdvance >= 14) {
            return applyDiscount(originalPrice, 10); // 10% discount for 14+ days advance
        } else if (daysInAdvance >= 7) {
            return applyDiscount(originalPrice, 5); // 5% discount for 7+ days advance
        }
        return originalPrice;
    }
    
    /**
     * Apply loyalty discount
     */
    public double applyLoyaltyDiscount(double originalPrice, String loyaltyTier) {
        return switch (loyaltyTier) {
            case "PLATINUM" -> applyDiscount(originalPrice, 20); // 20% discount
            case "GOLD" -> applyDiscount(originalPrice, 15); // 15% discount
            case "SILVER" -> applyDiscount(originalPrice, 10); // 10% discount
            case "BRONZE" -> applyDiscount(originalPrice, 5); // 5% discount
            default -> originalPrice; // No discount for non-members
        };
    }
    
    /**
     * Calculate group discount
     */
    public double applyGroupDiscount(double originalPrice, int numberOfRooms) {
        if (numberOfRooms >= 10) {
            return applyDiscount(originalPrice, 20); // 20% for 10+ rooms
        } else if (numberOfRooms >= 5) {
            return applyDiscount(originalPrice, 15); // 15% for 5-9 rooms
        } else if (numberOfRooms >= 3) {
            return applyDiscount(originalPrice, 10); // 10% for 3-4 rooms
        }
        return originalPrice;
    }
    
    /**
     * Calculate seasonal pricing
     */
    public double applySeasonalPricing(double basePrice, boolean isPeakSeason) {
        if (isPeakSeason) {
            return basePrice * PEAK_SEASON_MULTIPLIER;
        }
        return basePrice;
    }
    
    /**
     * Calculate price range for room type
     */
    public double[] getPriceRange(Room room, int minNights, int maxNights) {
        double minPrice = calculateTotalPrice(room, minNights);
        double maxPrice = calculateTotalPrice(room, maxNights);
        return new double[]{minPrice, maxPrice};
    }
    
    /**
     * Get tax rate
     */
    public double getTaxRate() {
        return TAX_RATE * 100;
    }
    
    /**
     * Calculate breakdown of total price
     */
    public PriceBreakdown calculatePriceBreakdown(Room room, int numberOfNights, double discountAmount) {
        double basePrice = calculateBasePrice(room, numberOfNights);
        double taxAmount = calculateTax(basePrice);
        double totalBeforeDiscount = basePrice + taxAmount;
        double totalAfterDiscount = totalBeforeDiscount - discountAmount;
        
        return new PriceBreakdown(basePrice, taxAmount, discountAmount, totalAfterDiscount);
    }
    
    /**
     * Inner class for price breakdown
     */
    public static class PriceBreakdown {
        public double basePrice;
        public double taxAmount;
        public double discountAmount;
        public double totalPrice;
        
        public PriceBreakdown(double basePrice, double taxAmount, double discountAmount, double totalPrice) {
            this.basePrice = basePrice;
            this.taxAmount = taxAmount;
            this.discountAmount = discountAmount;
            this.totalPrice = totalPrice;
        }
        
        public double getSubtotal() {
            return basePrice + taxAmount;
        }
    }
}
