package com.heaven.hotel.model.room;

/**
 * Suite room – premium accommodation with extended amenities.
 * OOP: Inheritance (extends Room), Polymorphism (calculatePrice / displayInfo overrides)
 */
public class SuiteRoom extends Room {

    private int    numberOfRooms;     // total rooms inside the suite
    private int    bathroomCount;
    private boolean hasJacuzzi;
    private boolean hasSeparateLounge;
    private double  priceMultiplier;  // applied on top of base price

    // Aliases kept for backward compatibility with serialiser
    public int getBedroomCount()                 { return numberOfRooms; }
    public void setBedroomCount(int n)           { this.numberOfRooms = n; }
    public int getBathroomCount()                { return bathroomCount; }
    public void setBathroomCount(int n)          { this.bathroomCount = n; }
    public double getPremiumMultiplier()         { return priceMultiplier; }
    public void setPremiumMultiplier(double m)   { this.priceMultiplier = m; }
    public boolean isHasSeparateLounge()         { return hasSeparateLounge; }
    public void setHasSeparateLounge(boolean v)  { this.hasSeparateLounge = v; }

    public SuiteRoom() { super(); }

    public SuiteRoom(String roomId, int roomNumber, double basePrice, String description) {
        super(roomId, roomNumber, "SUITE", 4, basePrice, description);
        this.numberOfRooms    = 2;
        this.bathroomCount    = 2;
        this.hasJacuzzi       = true;
        this.hasSeparateLounge = true;
        this.priceMultiplier  = 1.5;
    }

    // ── Polymorphic methods ───────────────────────────────────────────────────

    /** Suite price = base × multiplier × nights */
    @Override
    public double calculatePrice(int nights) {
        return getBasePrice() * priceMultiplier * nights;
    }

    /** Calculated nightly rate with multiplier applied. */
    @Override
    public double getCalculatedPrice() {
        return getBasePrice() * priceMultiplier;
    }

    @Override
    public String getAmenities() {
        StringBuilder sb = new StringBuilder(
                "Suite | Rooms: " + numberOfRooms + " | Bathrooms: " + bathroomCount + " | ");
        if (isHasAC())          sb.append("AC | ");
        if (isHasWifi())        sb.append("WiFi | ");
        if (isHasTV())          sb.append("TV | ");
        if (isHasKitchen())     sb.append("Kitchen | ");
        if (isHasBalcony())     sb.append("Balcony | ");
        if (hasJacuzzi)         sb.append("Jacuzzi | ");
        if (hasSeparateLounge)  sb.append("Lounge | ");
        return sb.toString().replaceAll("\\| $", "");
    }

    @Override
    public void displayInfo() {
        System.out.println("=== Suite Room ===");
        System.out.println("Room      : " + getRoomNumber() + "  (Floor " + getFloor() + ")");
        System.out.println("Rooms     : " + numberOfRooms + " bedroom(s), " + bathroomCount + " bathroom(s)");
        System.out.println("Base price: Rs. " + String.format("%,.0f", getBasePrice()) + " / night");
        System.out.println("Multiplier: " + priceMultiplier + "x  →  Rs. "
                + String.format("%,.0f", getCalculatedPrice()) + " / night");
        System.out.println("Status    : " + getStatus());
        System.out.println("Extras    : " + getAmenities());
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: SUITE|roomId|roomNumber|floor|status|basePrice|amenities|description|numberOfRooms|bathroomCount|hasJacuzzi|hasSeparateLounge|priceMultiplier
     */
    @Override
    public String toString() {
        return "SUITE|" + getRoomId() + "|" + getRoomNumber() + "|" + getFloor()
                + "|" + getStatus() + "|" + getBasePrice() + "|" + amenitiesString()
                + "|" + (getDescription() != null ? getDescription() : "")
                + "|" + numberOfRooms + "|" + bathroomCount
                + "|" + hasJacuzzi + "|" + hasSeparateLounge + "|" + priceMultiplier;
    }

    public static SuiteRoom fromString(String line) {
        String[] f = line.split("\\|", -1);
        SuiteRoom r = new SuiteRoom();
        if (f.length > 1) r.setRoomId(f[1]);
        if (f.length > 2) r.setRoomNumber(f[2]);
        if (f.length > 3) { try { r.setFloor(Integer.parseInt(f[3])); } catch (Exception ignored) {} }
        if (f.length > 4) r.setStatus(f[4]);
        if (f.length > 5) { try { r.setBasePrice(Double.parseDouble(f[5])); } catch (Exception ignored) {} }
        if (f.length > 6 && !f[6].isBlank())
            r.setAmenities(java.util.Arrays.asList(f[6].split(",")));
        if (f.length > 7) r.setDescription(f[7]);
        if (f.length > 8) { try { r.numberOfRooms = Integer.parseInt(f[8]); } catch (Exception ignored) { r.numberOfRooms = 2; } }
        if (f.length > 9) { try { r.bathroomCount = Integer.parseInt(f[9]); } catch (Exception ignored) { r.bathroomCount = 2; } }
        if (f.length > 10) r.hasJacuzzi = Boolean.parseBoolean(f[10]);
        if (f.length > 11) r.hasSeparateLounge = Boolean.parseBoolean(f[11]);
        if (f.length > 12) { try { r.priceMultiplier = Double.parseDouble(f[12]); } catch (Exception ignored) { r.priceMultiplier = 1.5; } }
        r.setRoomType("SUITE");
        return r;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int     getNumberOfRooms()          { return numberOfRooms; }
    public void    setNumberOfRooms(int n)     { this.numberOfRooms = n; }

    public boolean isHasJacuzzi()              { return hasJacuzzi; }
    public void    setHasJacuzzi(boolean v)    { this.hasJacuzzi = v; }

    public double  getPriceMultiplier()        { return priceMultiplier; }
    public void    setPriceMultiplier(double m){ this.priceMultiplier = m; }

    @Override public int getCapacity()         { return numberOfRooms * 2; }
}
