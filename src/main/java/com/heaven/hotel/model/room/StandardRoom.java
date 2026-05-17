package com.heaven.hotel.model.room;

import java.util.Arrays;

/**
 * Standard room – basic accommodation with essential amenities.
 * OOP: Inheritance (extends Room), Polymorphism (calculatePrice / displayInfo overrides)
 */
public class StandardRoom extends Room {

    private String  bedType;    // "Single", "Double", "Twin"
    private boolean hasBathtub;

    public StandardRoom() { super(); }

    public StandardRoom(String roomId, int roomNumber, double pricePerNight, String description) {
        super(roomId, roomNumber, "STANDARD", 2, pricePerNight, description);
        this.bedType    = "Double";
        this.hasBathtub = true;
    }

    // ── Polymorphic methods ───────────────────────────────────────────────────

    /** Returns total price for the given number of nights at base rate. */
    @Override
    public double calculatePrice(int nights) {
        return getBasePrice() * nights;
    }

    /** Standard rooms: no multiplier — calculated price equals base price. */
    @Override
    public double getCalculatedPrice() {
        return getBasePrice();
    }

    /** Formatted amenity string for display. */
    @Override
    public String getAmenities() {
        StringBuilder sb = new StringBuilder("Standard Room | Bed: " + bedType + " | ");
        if (isHasAC())     sb.append("AC | ");
        if (isHasWifi())   sb.append("WiFi | ");
        if (isHasTV())     sb.append("TV | ");
        if (hasBathtub)    sb.append("Bathtub | ");
        if (isHasBalcony()) sb.append("Balcony | ");
        return sb.toString().replaceAll("\\| $", "");
    }

    /** Prints formatted room details to console. */
    @Override
    public void displayInfo() {
        System.out.println("=== Standard Room ===");
        System.out.println("Room   : " + getRoomNumber() + "  (Floor " + getFloor() + ")");
        System.out.println("Bed    : " + bedType);
        System.out.println("Price  : Rs. " + String.format("%,.0f", getBasePrice()) + " / night");
        System.out.println("Status : " + getStatus());
        System.out.println("Extras : " + getAmenities());
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: STANDARD|roomId|roomNumber|floor|status|basePrice|amenities|description|bedType|hasBathtub
     */
    @Override
    public String toString() {
        return "STANDARD|" + getRoomId() + "|" + getRoomNumber() + "|" + getFloor()
                + "|" + getStatus() + "|" + getBasePrice() + "|" + amenitiesString()
                + "|" + (getDescription() != null ? getDescription() : "")
                + "|" + bedType + "|" + hasBathtub;
    }

    public static StandardRoom fromString(String line) {
        String[] f = line.split("\\|", -1);
        StandardRoom r = new StandardRoom();
        // f[0]=type, f[1]=roomId, f[2]=roomNumber, f[3]=floor, f[4]=status,
        // f[5]=basePrice, f[6]=amenities, f[7]=description, f[8]=bedType, f[9]=hasBathtub
        if (f.length > 1)  r.setRoomId(f[1]);
        if (f.length > 2)  r.setRoomNumber(f[2]);
        if (f.length > 3)  { try { r.setFloor(Integer.parseInt(f[3])); } catch (Exception ignored) {} }
        if (f.length > 4)  r.setStatus(f[4]);
        if (f.length > 5)  { try { r.setBasePrice(Double.parseDouble(f[5])); } catch (Exception ignored) {} }
        if (f.length > 6 && !f[6].isBlank())
            r.setAmenities(java.util.Arrays.asList(f[6].split(",")));
        if (f.length > 7)  r.setDescription(f[7]);
        if (f.length > 8)  r.setBedType(f[8].isBlank() ? "Double" : f[8]);
        if (f.length > 9)  r.setHasBathtub(Boolean.parseBoolean(f[9]));
        r.setRoomType("STANDARD");
        return r;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String  getBedType()              { return bedType; }
    public void    setBedType(String t)      { this.bedType = t; }

    public boolean isHasBathtub()            { return hasBathtub; }
    public void    setHasBathtub(boolean v)  { this.hasBathtub = v; }

    @Override public int getCapacity()       { return 2; }
}
