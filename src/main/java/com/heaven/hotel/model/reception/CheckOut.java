package com.heaven.hotel.model.reception;

import java.time.LocalDateTime;

public class CheckOut {
    private String checkOutId;
    private String bookingId;
    private String guestId;
    private String roomId;
    private LocalDateTime checkOutTime;
    private String staffId;
    private double additionalCharges;
    private String notes;

    public CheckOut() {}
    public CheckOut(String checkOutId, String bookingId, String guestId, String roomId, String staffId) {
        this.checkOutId = checkOutId; this.bookingId = bookingId; this.guestId = guestId;
        this.roomId = roomId; this.staffId = staffId; this.checkOutTime = LocalDateTime.now();
    }

    public String getCheckOutId() { return checkOutId; }
    public void setCheckOutId(String v) { this.checkOutId = v; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String v) { this.bookingId = v; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String v) { this.guestId = v; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String v) { this.roomId = v; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime v) { this.checkOutTime = v; }
    public String getStaffId() { return staffId; }
    public void setStaffId(String v) { this.staffId = v; }
    public double getAdditionalCharges() { return additionalCharges; }
    public void setAdditionalCharges(double v) { this.additionalCharges = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { this.notes = v; }
}
