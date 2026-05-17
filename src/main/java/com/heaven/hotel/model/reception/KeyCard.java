package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Hotel key card issued to a guest at check-in.
 * OOP: Encapsulation (isActive controlled via activate/deactivate only)
 */
public class KeyCard {

    private String cardId;
    private String roomId;
    private String guestId;
    private boolean isActive;
    private String issuedDate;   // YYYY-MM-DD HH:mm
    private String expiryDate;   // YYYY-MM-DD HH:mm (= check-out time)
    private String bookingId;
    private boolean reportedLost;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public KeyCard() {}

    public KeyCard(String cardId, String roomId, String bookingId, String guestId,
                   String issuedDate, String expiryDate) {
        this.cardId      = cardId;
        this.roomId      = roomId;
        this.bookingId   = bookingId;
        this.guestId     = guestId;
        this.issuedDate  = issuedDate;
        this.expiryDate  = expiryDate;
        this.isActive    = false;
    }

    /** Convenience constructor that activates immediately */
    public KeyCard(String cardId, String roomId, String bookingId, String guestId) {
        this.cardId     = cardId;
        this.roomId     = roomId;
        this.bookingId  = bookingId;
        this.guestId    = guestId;
        this.issuedDate = LocalDateTime.now().format(FMT);
        this.isActive   = true;
    }

    // ── Card operations ───────────────────────────────────────────────────────

    /** Activates the key card so it can open the assigned room. */
    public void activate() {
        this.isActive = true;
        System.out.println("Key card " + cardId + " activated for room " + roomId);
    }

    /** Deactivates the key card (e.g. on check-out). */
    public void deactivate() {
        this.isActive = false;
        System.out.println("Key card " + cardId + " deactivated");
    }

    /**
     * Deactivates the card and flags it as lost in the system.
     */
    public void reportLost() {
        this.isActive     = false;
        this.reportedLost = true;
        System.out.println("Key card " + cardId + " reported LOST — card deactivated");
    }

    /**
     * Returns true if the current time is past the card's expiry date.
     */
    public boolean isExpired() {
        if (expiryDate == null || expiryDate.isBlank()) return false;
        try {
            LocalDateTime expiry = LocalDateTime.parse(expiryDate, FMT);
            return LocalDateTime.now().isAfter(expiry);
        } catch (Exception e) {
            return false;
        }
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: cardId|roomId|guestId|isActive|issuedDate|expiryDate
     */
    @Override
    public String toString() {
        return cardId + "|" + roomId + "|" + nvl(guestId)
                + "|" + isActive
                + "|" + nvl(issuedDate)
                + "|" + nvl(expiryDate);
    }

    public static KeyCard fromString(String line) {
        String[] f = line.split("\\|", -1);
        KeyCard k = new KeyCard();
        if (f.length > 0) k.setCardId(f[0]);
        if (f.length > 1) k.setRoomId(f[1]);
        if (f.length > 2) k.setGuestId(f[2]);
        if (f.length > 3) k.setActive(Boolean.parseBoolean(f[3]));
        if (f.length > 4) k.setIssuedDate(f[4]);
        if (f.length > 5) k.setExpiryDate(f[5]);
        return k;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String  getCardId()                  { return cardId; }
    public void    setCardId(String id)         { this.cardId = id; }
    public String  getKeyCardId()               { return cardId; }
    public void    setKeyCardId(String id)      { this.cardId = id; }

    public String  getRoomId()                  { return roomId; }
    public void    setRoomId(String id)         { this.roomId = id; }

    public String  getGuestId()                 { return guestId; }
    public void    setGuestId(String id)        { this.guestId = id; }

    public boolean isActive()                   { return isActive && !isExpired(); }
    public void    setActive(boolean v)         { this.isActive = v; }

    public String  getIssuedDate()              { return issuedDate; }
    public void    setIssuedDate(String d)      { this.issuedDate = d; }

    public String  getExpiryDate()              { return expiryDate; }
    public void    setExpiryDate(String d)      { this.expiryDate = d; }

    public String  getBookingId()               { return bookingId; }
    public void    setBookingId(String id)      { this.bookingId = id; }

    public boolean isReportedLost()             { return reportedLost; }

    public String getStatus()                   { return isActive ? "ACTIVE" : (reportedLost ? "LOST" : "DEACTIVATED"); }
    public void   setStatus(String s)           { this.isActive = "ACTIVE".equalsIgnoreCase(s); }

    public LocalDateTime getIssuedAt() {
        try { return LocalDateTime.parse(issuedDate, FMT); } catch (Exception e) { return null; }
    }
    public void setIssuedAt(LocalDateTime v)    { this.issuedDate = v != null ? v.format(FMT) : ""; }

    public LocalDateTime getExpiresAt() {
        try { return LocalDateTime.parse(expiryDate, FMT); } catch (Exception e) { return null; }
    }
    public void setExpiresAt(LocalDateTime v)   { this.expiryDate = v != null ? v.format(FMT) : ""; }
}
