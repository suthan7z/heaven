package com.heaven.hotel.model.room;

import com.heaven.hotel.utils.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all room types.
 * OOP: Abstraction, Encapsulation, Polymorphism
 */
public abstract class Room {

    private String roomId;
    private String roomNumber;   // door number as String (e.g. "101")
    private String type;         // "Single", "Double", "Suite", "Deluxe"
    private int    floor;
    private String status;       // "Available", "Occupied", "Maintenance", "Reserved"
    private double basePrice;    // nightly base rate in LKR
    private List<String> amenities; // ["WiFi","AC","TV",...]
    private String description;

    // Boolean convenience flags (derived from amenities list for UI)
    private boolean hasAC;
    private boolean hasWifi;
    private boolean hasTV;
    private boolean hasKitchen;
    private boolean hasBalcony;

    private LocalDateTime createdAt;
    private LocalDateTime lastMaintenanceDate;
    private String maintenanceNotes;

    public Room() {}

    public Room(String roomId, String roomNumber, String type, int floor,
                String status, double basePrice, List<String> amenities) {
        this.roomId     = roomId;
        this.roomNumber = roomNumber;
        this.type       = type;
        this.floor      = floor;
        this.status     = status;
        this.basePrice  = basePrice;
        this.amenities  = amenities != null ? amenities : new ArrayList<>();
        this.createdAt  = LocalDateTime.now();
        syncFlagsFromAmenities();
    }

    /** Legacy constructor used by existing code */
    public Room(String roomId, int roomNumberInt, String type, int capacity,
                double pricePerNight, String description) {
        this.roomId      = roomId;
        this.roomNumber  = String.valueOf(roomNumberInt);
        this.type        = type;
        this.status      = Constants.ROOM_STATUS_AVAILABLE;
        this.basePrice   = pricePerNight;
        this.description = description;
        this.createdAt   = LocalDateTime.now();
        this.hasAC       = true;
        this.hasWifi     = true;
        this.amenities   = new ArrayList<>();
        buildAmenitiesFromFlags();
    }

    // ── Abstract methods (Polymorphism) ───────────────────────────────────────

    /** Returns human-readable amenity string — each subtype formats differently */
    public abstract String getAmenities();

    /** Polymorphic price calculation — subclass applies its own pricing rules */
    public abstract double getCalculatedPrice();

    /** Polymorphic price for a given number of nights */
    public abstract double calculatePrice(int nights);

    /** Polymorphic display — each subtype shows its own details */
    public abstract void displayInfo();

    /** Pipe-delimited string for file storage */
    @Override
    public abstract String toString();

    /** Factory method — delegates to the correct subclass */
    public static Room fromString(String line) {
        if (line == null || line.isBlank()) return null;
        String[] f = line.split("\\|", -1);
        if (f.length < 16) return null;
        String subtype = f[15].trim().toUpperCase();
        if ("SUITE".equals(subtype))    return SuiteRoom.fromString(line);
        return StandardRoom.fromString(line);
    }

    // ── Availability check (with date range) ─────────────────────────────────

    /**
     * Returns true if the room is free for the requested date range.
     * (Full calendar-based check requires cross-referencing bookings.txt.)
     */
    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        return Constants.ROOM_STATUS_AVAILABLE.equals(this.status);
    }

    // ── Tier system (price-based) ─────────────────────────────────────────────

    public String getTier() {
        double p = getCalculatedPrice();
        if (p < 8000)  return "BUDGET";
        if (p < 20000) return "STANDARD";
        if (p < 50000) return "LUXURY";
        return "PRESIDENTIAL";
    }

    public String getTierLabel() {
        switch (getTier()) {
            case "BUDGET":       return "Budget";
            case "STANDARD":     return "Standard";
            case "LUXURY":       return "Luxury";
            case "PRESIDENTIAL": return "Presidential";
            default:             return "Standard";
        }
    }

    public String getTierColor() {
        switch (getTier()) {
            case "BUDGET":       return "green";
            case "STANDARD":     return "blue";
            case "LUXURY":       return "purple";
            case "PRESIDENTIAL": return "amber";
            default:             return "blue";
        }
    }

    // ── Status helpers ────────────────────────────────────────────────────────

    public boolean isAvailable()       { return Constants.ROOM_STATUS_AVAILABLE.equals(status); }
    public boolean isOccupied()        { return Constants.ROOM_STATUS_OCCUPIED.equals(status); }
    public boolean isUnderMaintenance(){ return Constants.ROOM_STATUS_MAINTENANCE.equals(status); }
    public boolean isReserved()        { return Constants.ROOM_STATUS_RESERVED.equals(status); }

    public void markAvailable()  { this.status = Constants.ROOM_STATUS_AVAILABLE; }
    public void markOccupied()   { this.status = Constants.ROOM_STATUS_OCCUPIED; }
    public void markMaintenance(){ this.status = Constants.ROOM_STATUS_MAINTENANCE; this.lastMaintenanceDate = LocalDateTime.now(); }
    public void markReserved()   { this.status = Constants.ROOM_STATUS_RESERVED; }

    // ── Amenity list helpers ──────────────────────────────────────────────────

    public List<String> getAmenitiesList() {
        if (amenities == null || amenities.isEmpty()) buildAmenitiesFromFlags();
        return amenities;
    }

    protected void syncFlagsFromAmenities() {
        if (amenities == null) return;
        hasAC      = amenities.stream().anyMatch(a -> a.equalsIgnoreCase("AC"));
        hasWifi    = amenities.stream().anyMatch(a -> a.equalsIgnoreCase("WiFi"));
        hasTV      = amenities.stream().anyMatch(a -> a.equalsIgnoreCase("TV"));
        hasKitchen = amenities.stream().anyMatch(a -> a.equalsIgnoreCase("Kitchen"));
        hasBalcony = amenities.stream().anyMatch(a -> a.equalsIgnoreCase("Balcony"));
    }

    protected void buildAmenitiesFromFlags() {
        amenities = new ArrayList<>();
        if (hasAC)      amenities.add("AC");
        if (hasWifi)    amenities.add("WiFi");
        if (hasTV)      amenities.add("TV");
        if (hasKitchen) amenities.add("Kitchen");
        if (hasBalcony) amenities.add("Balcony");
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getRoomId()                           { return roomId; }
    public void   setRoomId(String id)                  { this.roomId = id; }

    public String getRoomNumber()                       { return roomNumber; }
    public void   setRoomNumber(String n)               { this.roomNumber = n; }
    public void   setRoomNumber(int n)                  { this.roomNumber = String.valueOf(n); }
    public int    getRoomNumberInt()                    { try { return Integer.parseInt(roomNumber); } catch (Exception e) { return 0; } }

    public String getType()                             { return type; }
    public void   setType(String t)                     { this.type = t; }
    public String getRoomType()                         { return type; }
    public void   setRoomType(String t)                 { this.type = t; }

    public int    getFloor()                            { return floor; }
    public void   setFloor(int f)                       { this.floor = f; }

    public String getStatus()                           { return status; }
    public void   setStatus(String s)                   { this.status = s; }

    public double getBasePrice()                        { return basePrice; }
    public void   setBasePrice(double p)                { this.basePrice = p; }
    public double getPricePerNight()                    { return basePrice; }
    public void   setPricePerNight(double p)            { this.basePrice = p; }

    public void   setAmenities(List<String> a)          { this.amenities = a; syncFlagsFromAmenities(); }

    public String getDescription()                      { return description; }
    public void   setDescription(String d)              { this.description = d; }

    public boolean isHasAC()                            { return hasAC; }
    public void    setHasAC(boolean v)                  { this.hasAC = v; buildAmenitiesFromFlags(); }

    public boolean isHasWifi()                          { return hasWifi; }
    public void    setHasWifi(boolean v)                { this.hasWifi = v; buildAmenitiesFromFlags(); }

    public boolean isHasTV()                            { return hasTV; }
    public void    setHasTV(boolean v)                  { this.hasTV = v; buildAmenitiesFromFlags(); }

    public boolean isHasKitchen()                       { return hasKitchen; }
    public void    setHasKitchen(boolean v)             { this.hasKitchen = v; buildAmenitiesFromFlags(); }

    public boolean isHasBalcony()                       { return hasBalcony; }
    public void    setHasBalcony(boolean v)             { this.hasBalcony = v; buildAmenitiesFromFlags(); }

    public int getCapacity()                            { return 2; } // default; overridden by subclasses
    public void setCapacity(int c)                      {}

    public LocalDateTime getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(LocalDateTime v)           { this.createdAt = v; }

    public LocalDateTime getLastMaintenanceDate()       { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDateTime v) { this.lastMaintenanceDate = v; }

    public String getMaintenanceNotes()                 { return maintenanceNotes; }
    public void   setMaintenanceNotes(String n)         { this.maintenanceNotes = n; }

    // ── Helper for serialisation ──────────────────────────────────────────────

    protected String amenitiesString() {
        buildAmenitiesFromFlags();
        return String.join(",", amenities);
    }
}
