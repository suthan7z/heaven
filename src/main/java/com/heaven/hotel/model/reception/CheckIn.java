package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Check-in record — logs a guest's arrival and room assignment.
 * OOP: Encapsulation (execute() bundles the entire check-in process), Abstraction
 */
public class CheckIn {

    private String checkInId;
    private String bookingId;
    private String guestId;
    private String staffId;
    private String roomAssigned;   // room number assigned at check-in
    private String actualArrival;  // YYYY-MM-DD HH:mm
    private String keyCardId;
    private String notes;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CheckIn() {}

    public CheckIn(String checkInId, String bookingId, String guestId,
                   String staffId, String roomAssigned, String actualArrival) {
        this.checkInId     = checkInId;
        this.bookingId     = bookingId;
        this.guestId       = guestId;
        this.staffId       = staffId;
        this.roomAssigned  = roomAssigned;
        this.actualArrival = actualArrival;
    }

    /** Convenience constructor with LocalDateTime */
    public CheckIn(String checkInId, String bookingId, String guestId,
                   String roomId, String staffId) {
        this(checkInId, bookingId, guestId, staffId, roomId,
             LocalDateTime.now().format(FMT));
    }

    // ── Core operations ───────────────────────────────────────────────────────

    /**
     * Runs the complete check-in process:
     * 1. Records actual arrival time
     * 2. Assigns room
     * 3. Issues key card
     * 4. Notifies housekeeping
     */
    public void execute() {
        this.actualArrival = LocalDateTime.now().format(FMT);
        assignRoom(roomAssigned);
        issueKeyCard(guestId, roomAssigned);
        notifyHousekeeping();
        System.out.println("✓ Check-in complete for guest " + guestId
                + " → Room " + roomAssigned + " | Card: " + keyCardId);
    }

    /**
     * Assigns the room and marks it as Occupied (status update done via RoomService in service layer).
     */
    public void assignRoom(String roomId) {
        this.roomAssigned = roomId;
        System.out.println("Room " + roomId + " assigned to guest " + guestId);
    }

    /**
     * Creates and activates a new KeyCard record for the guest.
     */
    public void issueKeyCard(String guestId, String roomId) {
        this.keyCardId = "KC" + System.currentTimeMillis();
        KeyCard card = new KeyCard(keyCardId, roomId, bookingId, guestId);
        card.activate();
        System.out.println("Key card " + keyCardId + " issued to guest " + guestId);
    }

    /**
     * Sends a room-ready notification to housekeeping staff.
     */
    public void notifyHousekeeping() {
        System.out.println("[Housekeeping] Room " + roomAssigned
                + " ready — guest " + guestId + " has arrived.");
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: checkInId|bookingId|guestId|staffId|roomAssigned|actualArrival|keyCardId
     */
    @Override
    public String toString() {
        return checkInId + "|" + bookingId + "|" + guestId + "|" + staffId
                + "|" + nvl(roomAssigned) + "|" + nvl(actualArrival) + "|" + nvl(keyCardId);
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getCheckInId()               { return checkInId; }
    public void   setCheckInId(String id)      { this.checkInId = id; }

    public String getBookingId()               { return bookingId; }
    public void   setBookingId(String id)      { this.bookingId = id; }

    public String getGuestId()                 { return guestId; }
    public void   setGuestId(String id)        { this.guestId = id; }

    public String getStaffId()                 { return staffId; }
    public void   setStaffId(String id)        { this.staffId = id; }

    public String getRoomAssigned()            { return roomAssigned; }
    public void   setRoomAssigned(String r)    { this.roomAssigned = r; }
    public String getRoomId()                  { return roomAssigned; }
    public void   setRoomId(String r)          { this.roomAssigned = r; }

    public String getActualArrival()           { return actualArrival; }
    public void   setActualArrival(String dt)  { this.actualArrival = dt; }

    public LocalDateTime getCheckInTime() {
        try { return LocalDateTime.parse(actualArrival, FMT); }
        catch (Exception e) { return null; }
    }
    public void setCheckInTime(LocalDateTime v){ this.actualArrival = v != null ? v.format(FMT) : ""; }

    public String getKeyCardId()               { return keyCardId; }
    public void   setKeyCardId(String id)      { this.keyCardId = id; }

    public String getNotes()                   { return notes; }
    public void   setNotes(String n)           { this.notes = n; }
}
