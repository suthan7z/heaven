package com.heaven.hotel.model.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Hotel reservation record.
 * OOP: Encapsulation (private fields, controlled state transitions), Abstraction (internal price calc)
 */
public class Booking {

    private String bookingId;
    private String guestId;
    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int    numberOfNights;
    private int    numberOfGuests;
    private double subtotal;
    private double discount;
    private double tax;
    private double total;
    private String status;          // "Pending","Confirmed","Cancelled","Completed"
    private LocalDateTime bookingDate;
    private String specialRequests;
    private boolean requiresPayment;
    private String paymentStatus;   // "UNPAID","PARTIALLY_PAID","PAID"
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String notes;

    private static final double TAX_RATE = 0.10; // 10% VAT

    public Booking() {}

    public Booking(String bookingId, String guestId, String roomId,
                   LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        this.bookingId      = bookingId;
        this.guestId        = guestId;
        this.roomId         = roomId;
        this.checkInDate    = checkInDate;
        this.checkOutDate   = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.bookingDate    = LocalDateTime.now();
        this.status         = "Pending";
        this.paymentStatus  = "UNPAID";
        this.requiresPayment = true;
        this.numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    // ── Calculation methods (Abstraction) ─────────────────────────────────────

    /** Number of nights between check-in and check-out. */
    public int calculateNights() {
        if (checkInDate == null || checkOutDate == null) return 0;
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    /** Base room cost for the full stay (before discount and tax). */
    private double calculateSubtotal(double pricePerNight) {
        return pricePerNight * calculateNights();
    }

    /** Discount: 5% if > 7 nights, 10% if > 14 nights. */
    public double calculateDiscount() {
        int nights = calculateNights();
        if (nights > 14) return subtotal * 0.10;
        if (nights > 7)  return subtotal * 0.05;
        return 0;
    }

    /** Tax applied after discount. */
    public double calculateTax() {
        return (subtotal - discount) * TAX_RATE;
    }

    /** Final payable = subtotal – discount + tax. */
    public double calculateTotal() {
        this.discount = calculateDiscount();
        this.tax      = calculateTax();
        this.total    = subtotal - discount + tax;
        return total;
    }

    /** Convenience method to set all pricing from a nightly room rate. */
    public void applyPricing(double pricePerNight) {
        this.subtotal = calculateSubtotal(pricePerNight);
        calculateTotal();
    }

    // ── Status transitions (Polymorphism-ready) ───────────────────────────────

    public void confirm() {
        this.status = "Confirmed";
    }

    public void cancel() {
        this.status = "Cancelled";
    }

    public void checkIn() {
        this.status      = "Confirmed"; // kept Confirmed; CheckIn service tracks actual arrival
        this.checkInTime = LocalDateTime.now();
    }

    public void checkOut() {
        this.status       = "Completed";
        this.checkOutTime = LocalDateTime.now();
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: bookingId|guestId|roomId|checkInDate|checkOutDate|status|guests|requests|subtotal|discount|tax|total
     */
    @Override
    public String toString() {
        return bookingId + "|" + guestId + "|" + roomId
                + "|" + (checkInDate  != null ? checkInDate  : "")
                + "|" + (checkOutDate != null ? checkOutDate : "")
                + "|" + status + "|" + numberOfGuests
                + "|" + (specialRequests != null ? specialRequests : "None")
                + "|" + subtotal + "|" + discount + "|" + tax + "|" + total;
    }

    public static Booking fromString(String line) {
        String[] f = line.split("\\|", -1);
        Booking b = new Booking();
        if (f.length > 0) b.setBookingId(f[0]);
        if (f.length > 1) b.setGuestId(f[1]);
        if (f.length > 2) b.setRoomId(f[2]);
        if (f.length > 3) { try { b.setCheckInDate(LocalDate.parse(f[3])); } catch (Exception ignored) {} }
        if (f.length > 4) { try { b.setCheckOutDate(LocalDate.parse(f[4])); } catch (Exception ignored) {} }
        if (f.length > 5) b.setStatus(f[5]);
        if (f.length > 6) { try { b.setNumberOfGuests(Integer.parseInt(f[6])); } catch (Exception ignored) {} }
        if (f.length > 7) b.setSpecialRequests(f[7]);
        if (f.length > 8)  { try { b.subtotal  = Double.parseDouble(f[8]);  } catch (Exception ignored) {} }
        if (f.length > 9)  { try { b.discount  = Double.parseDouble(f[9]);  } catch (Exception ignored) {} }
        if (f.length > 10) { try { b.tax       = Double.parseDouble(f[10]); } catch (Exception ignored) {} }
        if (f.length > 11) { try { b.total     = Double.parseDouble(f[11]); } catch (Exception ignored) {} }
        if (b.checkInDate != null && b.checkOutDate != null)
            b.numberOfNights = b.calculateNights();
        return b;
    }

    // ── Status helpers ────────────────────────────────────────────────────────

    public boolean isPending()     { return "Pending".equalsIgnoreCase(status) || "PENDING".equals(status); }
    public boolean isConfirmed()   { return "Confirmed".equalsIgnoreCase(status) || "CONFIRMED".equals(status); }
    public boolean isCheckedIn()   { return "CHECKED_IN".equalsIgnoreCase(status); }
    public boolean isCheckedOut()  { return "Completed".equalsIgnoreCase(status) || "CHECKED_OUT".equalsIgnoreCase(status); }
    public boolean isCancelled()   { return "Cancelled".equalsIgnoreCase(status) || "CANCELLED".equals(status); }
    public boolean isPaid()        { return "PAID".equals(paymentStatus); }
    public boolean isUnpaid()      { return "UNPAID".equals(paymentStatus); }
    public boolean isPartiallyPaid(){ return "PARTIALLY_PAID".equals(paymentStatus); }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String    getBookingId()                    { return bookingId; }
    public void      setBookingId(String id)           { this.bookingId = id; }

    public String    getGuestId()                      { return guestId; }
    public void      setGuestId(String id)             { this.guestId = id; }

    public String    getRoomId()                       { return roomId; }
    public void      setRoomId(String id)              { this.roomId = id; }

    public LocalDate getCheckInDate()                  { return checkInDate; }
    public void      setCheckInDate(LocalDate d)       { this.checkInDate = d; }

    public LocalDate getCheckOutDate()                 { return checkOutDate; }
    public void      setCheckOutDate(LocalDate d)      { this.checkOutDate = d; }

    public int       getNumberOfNights()               { return numberOfNights; }
    public void      setNumberOfNights(int n)          { this.numberOfNights = n; }

    public int       getNumberOfGuests()               { return numberOfGuests; }
    public void      setNumberOfGuests(int n)          { this.numberOfGuests = n; }

    public double    getSubtotal()                     { return subtotal; }
    public void      setSubtotal(double v)             { this.subtotal = v; }
    public double    getBasePrice()                    { return subtotal; }
    public void      setBasePrice(double v)            { this.subtotal = v; }

    public double    getDiscount()                     { return discount; }
    public void      setDiscount(double v)             { this.discount = v; }
    public double    getDiscountAmount()               { return discount; }
    public void      setDiscountAmount(double v)       { this.discount = v; }

    public double    getTax()                          { return tax; }
    public void      setTax(double v)                  { this.tax = v; }
    public double    getTaxAmount()                    { return tax; }
    public void      setTaxAmount(double v)            { this.tax = v; }

    public double    getTotal()                        { return total; }
    public void      setTotal(double v)                { this.total = v; }
    public double    getTotalPrice()                   { return total; }
    public void      setTotalPrice(double v)           { this.total = v; }

    public String    getStatus()                       { return status; }
    public void      setStatus(String s)               { this.status = s; }

    public LocalDateTime getBookingDate()              { return bookingDate; }
    public void setBookingDate(LocalDateTime v)        { this.bookingDate = v; }

    public String    getSpecialRequests()              { return specialRequests; }
    public void      setSpecialRequests(String r)      { this.specialRequests = r; }

    public boolean   isRequiresPayment()               { return requiresPayment; }
    public void      setRequiresPayment(boolean v)     { this.requiresPayment = v; }

    public String    getPaymentStatus()                { return paymentStatus; }
    public void      setPaymentStatus(String s)        { this.paymentStatus = s; }

    public LocalDateTime getCheckInTime()              { return checkInTime; }
    public void setCheckInTime(LocalDateTime v)        { this.checkInTime = v; }

    public LocalDateTime getCheckOutTime()             { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime v)       { this.checkOutTime = v; }

    public String    getNotes()                        { return notes; }
    public void      setNotes(String n)                { this.notes = n; }
}
