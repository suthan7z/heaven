package com.heaven.hotel.model.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Booking model representing a hotel reservation
 */
public class Booking {
    
    private String bookingId;
    private String guestId;
    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfNights;
    private int numberOfGuests;
    private double basePrice;
    private double taxAmount;
    private double discountAmount;
    private double totalPrice;
    private String status; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    private LocalDateTime bookingDate;
    private String specialRequests;
    private boolean requiresPayment;
    private String paymentStatus; // UNPAID, PARTIALLY_PAID, PAID
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String notes;
    
    public Booking() {}
    
    public Booking(String bookingId, String guestId, String roomId, 
                   LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.bookingDate = LocalDateTime.now();
        this.status = "PENDING";
        this.paymentStatus = "UNPAID";
        this.numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.requiresPayment = true;
    }
    
    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getGuestId() {
        return guestId;
    }
    
    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public int getNumberOfNights() {
        return numberOfNights;
    }
    
    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public boolean isRequiresPayment() {
        return requiresPayment;
    }
    
    public void setRequiresPayment(boolean requiresPayment) {
        this.requiresPayment = requiresPayment;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Status helpers
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }
    
    public boolean isConfirmed() {
        return "CONFIRMED".equals(this.status);
    }
    
    public boolean isCheckedIn() {
        return "CHECKED_IN".equals(this.status);
    }
    
    public boolean isCheckedOut() {
        return "CHECKED_OUT".equals(this.status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(this.status);
    }
    
    // Status transitions
    public void confirm() {
        this.status = "CONFIRMED";
    }
    
    public void checkIn() {
        this.status = "CHECKED_IN";
        this.checkInTime = LocalDateTime.now();
    }
    
    public void checkOut() {
        this.status = "CHECKED_OUT";
        this.checkOutTime = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = "CANCELLED";
    }
    
    // Payment status helpers
    public boolean isPaid() {
        return "PAID".equals(this.paymentStatus);
    }
    
    public boolean isUnpaid() {
        return "UNPAID".equals(this.paymentStatus);
    }
    
    public boolean isPartiallyPaid() {
        return "PARTIALLY_PAID".equals(this.paymentStatus);
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
