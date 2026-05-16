package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;

public class CheckIn {
    private String checkInId;
    private String bookingId;
    private String guestId;
    private String roomId;
    private LocalDateTime checkInTime;
    private String staffId;
    private String notes;

    public CheckIn() {}
    public CheckIn(String checkInId, String bookingId, String guestId, String roomId, String staffId) {
        this.checkInId = checkInId; this.bookingId = bookingId; this.guestId = guestId;
        this.roomId = roomId; this.staffId = staffId; this.checkInTime = LocalDateTime.now();
    }

    public String getCheckInId() { return checkInId; }
    public void setCheckInId(String v) { this.checkInId = v; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String v) { this.bookingId = v; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String v) { this.guestId = v; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String v) { this.roomId = v; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime v) { this.checkInTime = v; }
    public String getStaffId() { return staffId; }
    public void setStaffId(String v) { this.staffId = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { this.notes = v; }
}
