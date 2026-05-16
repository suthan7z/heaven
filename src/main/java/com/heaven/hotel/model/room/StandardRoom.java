package com.heaven.hotel.model.room;

/**
 * Standard room - basic accommodation with essential amenities
 */
public class StandardRoom extends Room {
    
    private boolean hasBathtub;
    
    public StandardRoom() {
        super();
    }
    
    public StandardRoom(String roomId, int roomNumber, double pricePerNight, String description) {
        super(roomId, roomNumber, "STANDARD", 2, pricePerNight, description);
        this.hasBathtub = true;
    }
    
    public boolean isHasBathtub() {
        return hasBathtub;
    }
    
    public void setHasBathtub(boolean hasBathtub) {
        this.hasBathtub = hasBathtub;
    }
    
    @Override
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Standard Room - ");
        amenities.append("Capacity: ").append(this.getCapacity()).append(" | ");
        
        if (this.isHasAC()) amenities.append("AC | ");
        if (this.isHasWifi()) amenities.append("WiFi | ");
        if (this.isHasTV()) amenities.append("TV | ");
        if (hasBathtub) amenities.append("Bathtub | ");
        
        return amenities.toString();
    }
    
    @Override
    public double getCalculatedPrice() {
        // Standard rooms are base price
        return this.getPricePerNight();
    }
    
    @Override
    public String toString() {
        return "StandardRoom{" +
                "roomNumber=" + this.getRoomNumber() +
                ", status='" + this.getStatus() + '\'' +
                ", price=" + this.getPricePerNight() +
                '}';
    }
}
