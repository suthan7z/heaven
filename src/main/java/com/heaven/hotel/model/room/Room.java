package com.heaven.hotel.model.room;

import com.heaven.hotel.utils.Constants;
import java.time.LocalDateTime;

/**
 * Abstract base class for rooms
 */
public abstract class Room {
    
    private String roomId;
    private int roomNumber;
    private String roomType; // STANDARD, SUITE
    private int capacity;
    private double pricePerNight;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    private String description;
    private boolean hasAC;
    private boolean hasWifi;
    private boolean hasTV;
    private boolean hasKitchen;
    private boolean hasBalcony;
    private LocalDateTime createdAt;
    private LocalDateTime lastMaintenanceDate;
    private String maintenanceNotes;
    
    public Room() {}
    
    public Room(String roomId, int roomNumber, String roomType, int capacity, 
                double pricePerNight, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = Constants.ROOM_STATUS_AVAILABLE;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.hasAC = true;
        this.hasWifi = true;
    }
    
    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isHasAC() {
        return hasAC;
    }
    
    public void setHasAC(boolean hasAC) {
        this.hasAC = hasAC;
    }
    
    public boolean isHasWifi() {
        return hasWifi;
    }
    
    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }
    
    public boolean isHasTV() {
        return hasTV;
    }
    
    public void setHasTV(boolean hasTV) {
        this.hasTV = hasTV;
    }
    
    public boolean isHasKitchen() {
        return hasKitchen;
    }
    
    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }
    
    public boolean isHasBalcony() {
        return hasBalcony;
    }
    
    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }
    
    public void setLastMaintenanceDate(LocalDateTime lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
    
    public String getMaintenanceNotes() {
        return maintenanceNotes;
    }
    
    public void setMaintenanceNotes(String maintenanceNotes) {
        this.maintenanceNotes = maintenanceNotes;
    }
    
    // Abstract methods for subclasses
    public abstract String getAmenities();
    
    public abstract double getCalculatedPrice();
    
    // Check room status
    public boolean isAvailable() {
        return Constants.ROOM_STATUS_AVAILABLE.equals(this.status);
    }
    
    public boolean isOccupied() {
        return Constants.ROOM_STATUS_OCCUPIED.equals(this.status);
    }
    
    public boolean isUnderMaintenance() {
        return Constants.ROOM_STATUS_MAINTENANCE.equals(this.status);
    }
    
    public boolean isReserved() {
        return Constants.ROOM_STATUS_RESERVED.equals(this.status);
    }
    
    // Mark room status
    public void markAvailable() {
        this.status = Constants.ROOM_STATUS_AVAILABLE;
    }
    
    public void markOccupied() {
        this.status = Constants.ROOM_STATUS_OCCUPIED;
    }
    
    public void markMaintenance() {
        this.status = Constants.ROOM_STATUS_MAINTENANCE;
        this.lastMaintenanceDate = LocalDateTime.now();
    }
    
    public void markReserved() {
        this.status = Constants.ROOM_STATUS_RESERVED;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomNumber=" + roomNumber +
                ", roomType='" + roomType + '\'' +
                ", status='" + status + '\'' +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
