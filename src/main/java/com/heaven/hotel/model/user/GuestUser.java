package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Guest user – can make bookings, view rooms, write reviews.
 * OOP: Inheritance (extends User), Polymorphism (login / hasPermission overrides)
 */
public class GuestUser extends User {

    private String preferences;            // e.g. "SeaView,NonSmoking"
    private List<String> bookingHistory;   // list of booking IDs
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private long   loyaltyPoints;
    private String loyaltyTier;            // BRONZE, SILVER, GOLD, PLATINUM

    public GuestUser() {
        super();
        this.role           = Constants.ROLE_GUEST;
        this.loyaltyTier    = "BRONZE";
        this.bookingHistory = new ArrayList<>();
    }

    public GuestUser(String username, String email, String contactNo,
                     String firstName, String lastName) {
        super(username, email, contactNo, firstName, lastName, Constants.ROLE_GUEST);
        this.loyaltyTier    = "BRONZE";
        this.loyaltyPoints  = 0;
        this.bookingHistory = new ArrayList<>();
    }

    // ── Polymorphic login ─────────────────────────────────────────────────────

    /**
     * Guest-specific authentication: account must be ACTIVE.
     * (Actual BCrypt verification is handled by Spring Security.)
     */
    @Override
    public boolean login(String username, String password) {
        return this.username.equalsIgnoreCase(username) && isActive();
    }

    // ── Abstract method implementations ──────────────────────────────────────

    @Override
    public String getDisplayName() { return getFullName() + " (Guest)"; }

    @Override
    public String getDefaultDashboardPage() { return "/dashboard"; }

    @Override
    public boolean hasPermission(String permissionCode) {
        switch (permissionCode) {
            case "view_rooms": case "make_booking": case "view_booking":
            case "cancel_booking": case "write_review": case "view_profile":
            case "edit_profile":   case "view_loyalty":
                return true;
            default: return false;
        }
    }

    // ── Booking history ───────────────────────────────────────────────────────

    public List<String> getBookingHistory()     { return bookingHistory; }

    public void addBooking(String bookingId) {
        if (bookingHistory == null) bookingHistory = new ArrayList<>();
        bookingHistory.add(bookingId);
    }

    // ── Loyalty helpers ───────────────────────────────────────────────────────

    public void addLoyaltyPoints(long points) {
        this.loyaltyPoints += points;
        updateLoyaltyTier();
    }

    private void updateLoyaltyTier() {
        if      (loyaltyPoints >= 10000) loyaltyTier = "PLATINUM";
        else if (loyaltyPoints >= 5000)  loyaltyTier = "GOLD";
        else if (loyaltyPoints >= 1000)  loyaltyTier = "SILVER";
        else                             loyaltyTier = "BRONZE";
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: userId|username|password|email|contactNo|GUEST|status|loyaltyPoints|loyaltyTier|preferences|bookingHistory
     */
    @Override
    public String toString() {
        String history = bookingHistory != null ? String.join(",", bookingHistory) : "";
        return userId + "|" + username + "|" + password + "|" + email + "|" + contactNo
                + "|" + role + "|" + status + "|" + loyaltyPoints + "|" + loyaltyTier
                + "|" + (preferences != null ? preferences : "")
                + "|" + history;
    }

    public static GuestUser fromString(String line) {
        String[] f = line.split("\\|", -1);
        GuestUser g = new GuestUser();
        if (f.length > 0)  g.setUserId(f[0]);
        if (f.length > 1)  g.setUsername(f[1]);
        if (f.length > 2)  g.setPassword(f[2]);
        if (f.length > 3)  g.setEmail(f[3]);
        if (f.length > 4)  g.setContactNo(f[4]);
        // f[5] = role, f[6] = status
        if (f.length > 6)  g.setStatus(f[6]);
        if (f.length > 7)  g.setLoyaltyPoints(parseLong(f[7]));
        if (f.length > 8)  g.setLoyaltyTier(f[8].isBlank() ? "BRONZE" : f[8]);
        if (f.length > 9)  g.setPreferences(f[9]);
        if (f.length > 10 && !f[10].isBlank())
            g.bookingHistory = new ArrayList<>(Arrays.asList(f[10].split(",")));
        return g;
    }

    private static long parseLong(String s) {
        try { return Long.parseLong(s); } catch (Exception e) { return 0; }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getPreferences()             { return preferences; }
    public void   setPreferences(String p)     { this.preferences = p; }

    public String getAddress()                 { return address; }
    public void   setAddress(String a)         { this.address = a; }

    public String getCity()                    { return city; }
    public void   setCity(String c)            { this.city = c; }

    public String getCountry()                 { return country; }
    public void   setCountry(String c)         { this.country = c; }

    public String getPostalCode()              { return postalCode; }
    public void   setPostalCode(String p)      { this.postalCode = p; }

    public long   getLoyaltyPoints()           { return loyaltyPoints; }
    public void   setLoyaltyPoints(long pts)   { this.loyaltyPoints = pts; }

    public String getLoyaltyTier()             { return loyaltyTier; }
    public void   setLoyaltyTier(String t)     { this.loyaltyTier = t; }
}
