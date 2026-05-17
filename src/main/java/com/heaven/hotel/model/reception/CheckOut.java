package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Check-out record — finalises a guest's stay.
 * OOP: Encapsulation (execute() bundles the entire check-out process), Abstraction
 */
public class CheckOut {

    private String checkOutId;
    private String bookingId;
    private String guestId;
    private String staffId;
    private String actualDeparture;    // YYYY-MM-DD HH:mm
    private double outstandingCharges; // extra charges (room service, late fee, etc.)
    private double finalTotal;         // total settled at check-out
    private String roomId;
    private String notes;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CheckOut() {}

    public CheckOut(String checkOutId, String bookingId, String guestId,
                    String staffId, String actualDeparture) {
        this.checkOutId      = checkOutId;
        this.bookingId       = bookingId;
        this.guestId         = guestId;
        this.staffId         = staffId;
        this.actualDeparture = actualDeparture;
    }

    /** Convenience constructor — sets departure to now and stores roomId */
    public CheckOut(String checkOutId, String bookingId, String guestId,
                    String roomId, String staffId, boolean autoTime) {
        this(checkOutId, bookingId, guestId, staffId,
             LocalDateTime.now().format(FMT));
        this.roomId = roomId;
    }

    // ── Core operations ───────────────────────────────────────────────────────

    /**
     * Runs the complete check-out process:
     * 1. Records departure time
     * 2. Collects any outstanding payment
     * 3. Releases the room
     * 4. Deactivates the key card
     */
    public void execute() {
        this.actualDeparture = LocalDateTime.now().format(FMT);
        this.finalTotal = outstandingCharges; // base booking total added by service layer
        collectPayment(outstandingCharges);
        releaseRoom(roomId);
        System.out.println("✓ Check-out complete for guest " + guestId
                + " | Outstanding: LKR " + String.format("%,.2f", outstandingCharges)
                + " | Total: LKR " + String.format("%,.2f", finalTotal));
    }

    /**
     * Processes any outstanding balance at check-out.
     */
    public void collectPayment(double amount) {
        if (amount > 0)
            System.out.println("Payment collected: LKR " + String.format("%,.2f", amount));
    }

    /**
     * Sets the room status back to Available (actual update done in service layer).
     */
    public void releaseRoom(String roomId) {
        this.roomId = roomId;
        System.out.println("Room " + roomId + " released → Available");
    }

    /**
     * Deactivates the guest's key card on departure.
     */
    public void deactivateKeyCard(String cardId) {
        System.out.println("Key card " + cardId + " deactivated for guest " + guestId);
    }

    /**
     * Returns the finalised invoice string for the entire stay.
     */
    public String generateFinalInvoice() {
        String line = "=".repeat(50);
        return line + "\n"
                + "        HEAVEN HOTEL — FINAL INVOICE\n"
                + line + "\n"
                + "Check-Out ID : " + checkOutId + "\n"
                + "Booking ID   : " + bookingId + "\n"
                + "Guest ID     : " + guestId + "\n"
                + "Departure    : " + actualDeparture + "\n"
                + "Extra charges: LKR " + String.format("%,.2f", outstandingCharges) + "\n"
                + "TOTAL SETTLED: LKR " + String.format("%,.2f", finalTotal) + "\n"
                + line + "\n"
                + "Thank you for staying at Heaven Hotel!\n"
                + line;
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: checkOutId|bookingId|guestId|staffId|actualDeparture|outstandingCharges|finalTotal
     */
    @Override
    public String toString() {
        return checkOutId + "|" + bookingId + "|" + guestId + "|" + staffId
                + "|" + nvl(actualDeparture)
                + "|" + outstandingCharges + "|" + finalTotal;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getCheckOutId()                  { return checkOutId; }
    public void   setCheckOutId(String id)         { this.checkOutId = id; }

    public String getBookingId()                   { return bookingId; }
    public void   setBookingId(String id)          { this.bookingId = id; }

    public String getGuestId()                     { return guestId; }
    public void   setGuestId(String id)            { this.guestId = id; }

    public String getStaffId()                     { return staffId; }
    public void   setStaffId(String id)            { this.staffId = id; }

    public String getActualDeparture()             { return actualDeparture; }
    public void   setActualDeparture(String dt)    { this.actualDeparture = dt; }

    public LocalDateTime getCheckOutTime() {
        try { return LocalDateTime.parse(actualDeparture, FMT); }
        catch (Exception e) { return null; }
    }
    public void setCheckOutTime(LocalDateTime v)   { this.actualDeparture = v != null ? v.format(FMT) : ""; }

    public double getOutstandingCharges()           { return outstandingCharges; }
    public void   setOutstandingCharges(double v)   { this.outstandingCharges = v; }
    public double getAdditionalCharges()            { return outstandingCharges; }
    public void   setAdditionalCharges(double v)    { this.outstandingCharges = v; }

    public double getFinalTotal()                   { return finalTotal; }
    public void   setFinalTotal(double v)           { this.finalTotal = v; }

    public String getRoomId()                       { return roomId; }
    public void   setRoomId(String id)              { this.roomId = id; }

    public String getNotes()                        { return notes; }
    public void   setNotes(String n)                { this.notes = n; }
}
