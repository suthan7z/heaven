package com.heaven.hotel.model.review;

import java.time.LocalDateTime;

public class LoyaltyProgram {
    private String programId;
    private String guestId;
    private long totalPoints;
    private String tier; // BRONZE, SILVER, GOLD, PLATINUM
    private LocalDateTime enrolledAt;
    private LocalDateTime lastActivityAt;

    public LoyaltyProgram() {}
    public LoyaltyProgram(String programId, String guestId) {
        this.programId = programId; this.guestId = guestId;
        this.totalPoints = 0; this.tier = "BRONZE";
        this.enrolledAt = LocalDateTime.now(); this.lastActivityAt = LocalDateTime.now();
    }

    public String getProgramId() { return programId; }
    public void setProgramId(String v) { this.programId = v; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String v) { this.guestId = v; }
    public long getTotalPoints() { return totalPoints; }
    public void setTotalPoints(long v) { this.totalPoints = v; }
    public String getTier() { return tier; }
    public void setTier(String v) { this.tier = v; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime v) { this.enrolledAt = v; }
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime v) { this.lastActivityAt = v; }

    public void addPoints(long points) {
        this.totalPoints += points;
        this.lastActivityAt = LocalDateTime.now();
        updateTier();
    }

    private void updateTier() {
        if (totalPoints >= 10000) tier = "PLATINUM";
        else if (totalPoints >= 5000) tier = "GOLD";
        else if (totalPoints >= 1000) tier = "SILVER";
        else tier = "BRONZE";
    }
}
