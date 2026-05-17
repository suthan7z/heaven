package com.heaven.hotel.model.admin;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Hotel-wide configuration settings.
 * OOP: Encapsulation (settings only changed via update() after validate())
 */
public class HotelConfig {

    private String configId;
    private String hotelName;
    private String address;
    private String phone;
    private String email;
    private String checkInTime;         // e.g. "14:00"
    private String checkOutTime;        // e.g. "12:00"
    private double taxRate;             // e.g. 0.10 for 10%
    private String cancellationPolicy;  // plain-text cancellation terms
    private int    maxRoomCapacity;

    private static final String CONFIG_FILE = "src/main/resources/data/hotel_config.txt";

    public HotelConfig() {
        // Defaults
        this.hotelName          = "Heaven Hotel";
        this.address            = "No. 12, KKS Road, Jaffna 40000, Sri Lanka";
        this.phone              = "+94 21 222 5678";
        this.email              = "reservations@heavenhotel.lk";
        this.checkInTime        = "14:00";
        this.checkOutTime       = "12:00";
        this.taxRate            = 0.10;
        this.maxRoomCapacity    = 6;
        this.cancellationPolicy = "Cancellations made 48+ hours before check-in receive a full refund. "
                + "Cancellations within 48 hours are charged one night's rate.";
    }

    // ── Config operations ─────────────────────────────────────────────────────

    /**
     * Validates all field values and saves to hotel_config.txt.
     */
    public void update() {
        if (!validate()) {
            System.err.println("Config validation failed — changes not saved.");
            return;
        }
        try (FileWriter fw = new FileWriter(CONFIG_FILE)) {
            fw.write(toString());
            System.out.println("Hotel configuration saved.");
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    /**
     * Checks all field values are within accepted bounds.
     * @return true if valid
     */
    public boolean validate() {
        if (hotelName == null || hotelName.isBlank()) {
            System.err.println("Hotel name is required."); return false;
        }
        if (taxRate < 0 || taxRate > 1) {
            System.err.println("Tax rate must be between 0 and 1."); return false;
        }
        if (maxRoomCapacity < 1 || maxRoomCapacity > 20) {
            System.err.println("Max room capacity must be 1–20."); return false;
        }
        return true;
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: configId|hotelName|address|phone|email|checkInTime|checkOutTime|taxRate|maxRoomCapacity|cancellationPolicy
     */
    @Override
    public String toString() {
        return nvl(configId) + "|" + nvl(hotelName) + "|" + nvl(address)
                + "|" + nvl(phone) + "|" + nvl(email)
                + "|" + nvl(checkInTime) + "|" + nvl(checkOutTime)
                + "|" + taxRate + "|" + maxRoomCapacity
                + "|" + nvl(cancellationPolicy);
    }

    public static HotelConfig fromString(String line) {
        String[] f = line.split("\\|", -1);
        HotelConfig c = new HotelConfig();
        if (f.length > 0)  c.setConfigId(f[0]);
        if (f.length > 1)  c.setHotelName(f[1]);
        if (f.length > 2)  c.setAddress(f[2]);
        if (f.length > 3)  c.setPhone(f[3]);
        if (f.length > 4)  c.setEmail(f[4]);
        if (f.length > 5)  c.setCheckInTime(f[5]);
        if (f.length > 6)  c.setCheckOutTime(f[6]);
        if (f.length > 7)  { try { c.setTaxRate(Double.parseDouble(f[7])); } catch (Exception ignored) {} }
        if (f.length > 8)  { try { c.setMaxRoomCapacity(Integer.parseInt(f[8])); } catch (Exception ignored) {} }
        if (f.length > 9)  c.setCancellationPolicy(f[9]);
        return c;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getConfigId()                     { return configId; }
    public void   setConfigId(String id)            { this.configId = id; }

    public String getHotelName()                    { return hotelName; }
    public void   setHotelName(String n)            { this.hotelName = n; }

    public String getAddress()                      { return address; }
    public void   setAddress(String a)              { this.address = a; }

    public String getPhone()                        { return phone; }
    public void   setPhone(String p)                { this.phone = p; }

    public String getEmail()                        { return email; }
    public void   setEmail(String e)                { this.email = e; }

    public String getCheckInTime()                  { return checkInTime; }
    public void   setCheckInTime(String t)          { this.checkInTime = t; }
    public int    getCheckInHour()                  { return parseHour(checkInTime); }
    public void   setCheckInHour(int h)             { this.checkInTime = h + ":00"; }

    public String getCheckOutTime()                 { return checkOutTime; }
    public void   setCheckOutTime(String t)         { this.checkOutTime = t; }
    public int    getCheckOutHour()                 { return parseHour(checkOutTime); }
    public void   setCheckOutHour(int h)            { this.checkOutTime = h + ":00"; }

    public double getTaxRate()                      { return taxRate; }
    public void   setTaxRate(double r)              { this.taxRate = r; }

    public String getCancellationPolicy()           { return cancellationPolicy; }
    public void   setCancellationPolicy(String p)   { this.cancellationPolicy = p; }

    public int    getMaxRoomCapacity()              { return maxRoomCapacity; }
    public void   setMaxRoomCapacity(int c)         { this.maxRoomCapacity = c; }

    private int parseHour(String time) {
        try { return Integer.parseInt(time.split(":")[0]); } catch (Exception e) { return 12; }
    }
}
