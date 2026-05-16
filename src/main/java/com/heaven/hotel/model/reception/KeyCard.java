package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;

public class KeyCard {
    private String keyCardId;
    private String roomId;
    private String bookingId;
    private String guestId;
    private String status; // ACTIVE, DEACTIVATED, LOST
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    public KeyCard() {}
    public KeyCard(String keyCardId, String roomId, String bookingId, String guestId) {
        this.keyCardId = keyCardId; this.roomId = roomId;
        this.bookingId = bookingId; this.guestId = guestId;
        this.status = "ACTIVE"; this.issuedAt = LocalDateTime.now();
    }

    public String getKeyCardId() { return keyCardId; }
    public void setKeyCardId(String v) { this.keyCardId = v; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String v) { this.roomId = v; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String v) { this.bookingId = v; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String v) { this.guestId = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime v) { this.issuedAt = v; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
    public boolean isActive() { return "ACTIVE".equals(status); }
}
