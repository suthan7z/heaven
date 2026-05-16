package com.heaven.hotel.model.room;

/**
 * Suite room - premium accommodation with extended amenities
 */
public class SuiteRoom extends Room {
    
    private int bedroomCount;
    private int bathroomCount;
    private boolean hasJacuzzi;
    private boolean hasSeparateLounge;
    private double premiumMultiplier; // Price multiplier for suites
    
    public SuiteRoom() {
        super();
    }
    
    public SuiteRoom(String roomId, int roomNumber, double basePrice, String description) {
        super(roomId, roomNumber, "SUITE", 4, basePrice, description);
        this.bedroomCount = 2;
        this.bathroomCount = 2;
        this.hasJacuzzi = true;
        this.hasSeparateLounge = true;
        this.premiumMultiplier = 1.5;
    }
    
    public int getBedroomCount() {
        return bedroomCount;
    }
    
    public void setBedroomCount(int bedroomCount) {
        this.bedroomCount = bedroomCount;
    }
    
    public int getBathroomCount() {
        return bathroomCount;
    }
    
    public void setBathroomCount(int bathroomCount) {
        this.bathroomCount = bathroomCount;
    }
    
    public boolean isHasJacuzzi() {
        return hasJacuzzi;
    }
    
    public void setHasJacuzzi(boolean hasJacuzzi) {
        this.hasJacuzzi = hasJacuzzi;
    }
    
    public boolean isHasSeparateLounge() {
        return hasSeparateLounge;
    }
    
    public void setHasSeparateLounge(boolean hasSeparateLounge) {
        this.hasSeparateLounge = hasSeparateLounge;
    }
    
    public double getPremiumMultiplier() {
        return premiumMultiplier;
    }
    
    public void setPremiumMultiplier(double premiumMultiplier) {
        this.premiumMultiplier = premiumMultiplier;
    }
    
    @Override
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Suite Room - ");
        amenities.append("Bedrooms: ").append(bedroomCount).append(" | ");
        amenities.append("Bathrooms: ").append(bathroomCount).append(" | ");
        
        if (this.isHasAC()) amenities.append("AC | ");
        if (this.isHasWifi()) amenities.append("WiFi | ");
        if (this.isHasTV()) amenities.append("TV | ");
        if (this.isHasKitchen()) amenities.append("Kitchen | ");
        if (this.isHasBalcony()) amenities.append("Balcony | ");
        if (hasJacuzzi) amenities.append("Jacuzzi | ");
        if (hasSeparateLounge) amenities.append("Lounge | ");
        
        return amenities.toString();
    }
    
    @Override
    public double getCalculatedPrice() {
        return this.getPricePerNight() * premiumMultiplier;
    }
    
    @Override
    public String toString() {
        return "SuiteRoom{" +
                "roomNumber=" + this.getRoomNumber() +
                ", bedrooms=" + bedroomCount +
                ", status='" + this.getStatus() + '\'' +
                ", basePrice=" + this.getPricePerNight() +
                ", calculatedPrice=" + getCalculatedPrice() +
                '}';
    }
}
