package com.heaven.hotel.model.review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Loyalty programme account for a guest.
 * Guests earn points from bookings and redeem them for discounts.
 */
public class LoyaltyProgram {

    private String guestId;
    private int    points;   // current balance (spec uses int)
    private String tier;     // "Bronze","Silver","Gold","Platinum"

    // extra tracking fields
    private String programId;
    private LocalDateTime enrolledAt;
    private LocalDateTime lastActivityAt;

    // Tier thresholds
    private static final int SILVER_THRESHOLD   = 1000;
    private static final int GOLD_THRESHOLD     = 5000;
    private static final int PLATINUM_THRESHOLD = 10000;

    // Points earned per 1,000 LKR spent
    private static final double POINTS_PER_LKR = 0.01; // 1 point per 100 LKR

    public LoyaltyProgram() {}

    public LoyaltyProgram(String guestId, int points, String tier) {
        this.guestId      = guestId;
        this.points       = points;
        this.tier         = tier;
        this.enrolledAt   = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    // ── Point operations ──────────────────────────────────────────────────────

    /**
     * Calculates and adds points based on the booking total value.
     * Rule: 1 point per 100 LKR spent.
     */
    public void earnPoints(double bookingTotal) {
        int earned = (int) (bookingTotal * POINTS_PER_LKR);
        this.points          += earned;
        this.lastActivityAt  = LocalDateTime.now();
        upgradeTier();
        System.out.println("Earned " + earned + " points. Balance: " + points + " | Tier: " + tier);
    }

    /**
     * Deducts points and returns the equivalent discount value in LKR.
     * Rule: 100 points = LKR 100 discount.
     */
    public double redeemPoints(int amount) {
        if (amount <= 0 || amount > this.points) {
            System.out.println("Insufficient points. Available: " + this.points);
            return 0;
        }
        this.points -= amount;
        this.lastActivityAt = LocalDateTime.now();
        double discount = amount; // 1 point = LKR 1
        System.out.println("Redeemed " + amount + " points → LKR " + discount + " discount. Remaining: " + points);
        return discount;
    }

    /**
     * Checks the current balance and upgrades tier if a threshold is reached.
     */
    public void upgradeTier() {
        String oldTier = this.tier;
        if      (points >= PLATINUM_THRESHOLD) tier = "Platinum";
        else if (points >= GOLD_THRESHOLD)     tier = "Gold";
        else if (points >= SILVER_THRESHOLD)   tier = "Silver";
        else                                   tier = "Bronze";
        if (!tier.equals(oldTier))
            System.out.println("Tier upgraded: " + oldTier + " → " + tier);
    }

    /**
     * Returns list of perks available for the current tier.
     */
    public List<String> getPerks() {
        List<String> perks = new ArrayList<>();
        perks.add("Earn loyalty points on every stay");
        switch (tier) {
            case "Platinum":
                perks.add("20% discount on all bookings");
                perks.add("Complimentary airport transfer");
                perks.add("Free room upgrade when available");
                perks.add("Dedicated concierge service");
                // fall through
            case "Gold":
                perks.add("15% discount on bookings");
                perks.add("Late check-out until 2PM");
                perks.add("Complimentary breakfast");
                // fall through
            case "Silver":
                perks.add("10% discount on bookings");
                perks.add("Early check-in from 12PM");
                perks.add("Welcome fruit basket");
                break;
            default: // Bronze
                perks.add("5% discount on bookings");
                perks.add("Welcome drink on arrival");
        }
        return perks;
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /** Format: guestId|points|tier */
    @Override
    public String toString() {
        return guestId + "|" + points + "|" + tier;
    }

    public static LoyaltyProgram fromString(String line) {
        String[] f = line.split("\\|", -1);
        LoyaltyProgram lp = new LoyaltyProgram();
        if (f.length > 0) lp.setGuestId(f[0]);
        if (f.length > 1) { try { lp.setPoints(Integer.parseInt(f[1])); } catch (Exception ignored) {} }
        if (f.length > 2) lp.setTier(f[2]);
        return lp;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getGuestId()               { return guestId; }
    public void   setGuestId(String id)      { this.guestId = id; }

    public int    getPoints()                { return points; }
    public void   setPoints(int p)           { this.points = p; }
    public long   getTotalPoints()           { return points; }
    public void   setTotalPoints(long p)     { this.points = (int) p; }

    public String getTier()                  { return tier; }
    public void   setTier(String t)          { this.tier = t; }

    public String getProgramId()             { return programId; }
    public void   setProgramId(String id)    { this.programId = id; }

    public LocalDateTime getEnrolledAt()     { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime v){ this.enrolledAt = v; }

    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime v){ this.lastActivityAt = v; }

    public void addPoints(long p)            { this.points += p; upgradeTier(); }
}
