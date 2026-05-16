package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;

/**
 * Guest user - can make bookings, view rooms, write reviews
 */
public class GuestUser extends User {
    
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private long loyaltyPoints;
    private String loyaltyTier;
    
    // Constructor
    public GuestUser() {
        super();
        this.role = Constants.ROLE_GUEST;
        this.loyaltyTier = "BRONZE";
    }
    
    public GuestUser(String username, String email, String phone, String firstName, String lastName) {
        super(username, email, phone, firstName, lastName, Constants.ROLE_GUEST);
        this.loyaltyTier = "BRONZE";
        this.loyaltyPoints = 0;
    }
    
    @Override
    public String getDisplayName() {
        return getFullName() + " (Guest)";
    }
    
    @Override
    public String getDefaultDashboardPage() {
        return "/dashboard";
    }
    
    @Override
    public boolean hasPermission(String permissionCode) {
        switch (permissionCode) {
            case "view_rooms":
            case "make_booking":
            case "view_booking":
            case "cancel_booking":
            case "write_review":
            case "view_profile":
            case "edit_profile":
            case "view_loyalty":
                return true;
            default:
                return false;
        }
    }
    
    // Getters and Setters
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public long getLoyaltyPoints() {
        return loyaltyPoints;
    }
    
    public void setLoyaltyPoints(long loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    
    public String getLoyaltyTier() {
        return loyaltyTier;
    }
    
    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }
    
    public void addLoyaltyPoints(long points) {
        this.loyaltyPoints += points;
        updateLoyaltyTier();
    }
    
    private void updateLoyaltyTier() {
        if (loyaltyPoints >= 10000) {
            this.loyaltyTier = "PLATINUM";
        } else if (loyaltyPoints >= 5000) {
            this.loyaltyTier = "GOLD";
        } else if (loyaltyPoints >= 1000) {
            this.loyaltyTier = "SILVER";
        } else {
            this.loyaltyTier = "BRONZE";
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " GuestUser{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", loyaltyTier='" + loyaltyTier + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }
}
