package com.heaven.hotel.model.booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payment transaction linked to a booking.
 * OOP: Encapsulation (controlled status via processPayment/refund)
 */
public class Payment {

    private String paymentId;
    private String bookingId;
    private String guestId;
    private double amount;
    private double amountPaid;
    private String currency;
    private String method;           // "Cash","Card","Online"
    private String status;           // "Pending","Completed","Refunded"
    private LocalDateTime paymentDate;
    private LocalDateTime transactionDate;
    private String transactionId;
    private String notes;
    private boolean isRefunded;
    private double  refundAmount;
    private LocalDateTime refundDate;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Payment() {}

    public Payment(String paymentId, String bookingId, String guestId, double amount) {
        this.paymentId   = paymentId;
        this.bookingId   = bookingId;
        this.guestId     = guestId;
        this.amount      = amount;
        this.amountPaid  = 0;
        this.currency    = "LKR";
        this.status      = "Pending";
        this.paymentDate = LocalDateTime.now();
        this.isRefunded  = false;
    }

    public Payment(String paymentId, String bookingId, double amount,
                   String method, String status, String transactionDate) {
        this.paymentId       = paymentId;
        this.bookingId       = bookingId;
        this.amount          = amount;
        this.method          = method;
        this.status          = status;
        this.currency        = "LKR";
        try { this.transactionDate = LocalDateTime.parse(transactionDate, FMT); }
        catch (Exception ignored) {}
    }

    // ── Payment operations ────────────────────────────────────────────────────

    /**
     * Marks the payment as Completed and records the transaction timestamp.
     */
    public void processPayment() {
        this.status          = "Completed";
        this.transactionDate = LocalDateTime.now();
        this.amountPaid      = this.amount;
        this.transactionId   = "TXN" + System.currentTimeMillis();
    }

    /** Marks payment as Completed with a given transaction reference. */
    public void markCompleted(String txnId) {
        this.status          = "Completed";
        this.transactionDate = LocalDateTime.now();
        this.transactionId   = txnId;
        this.amountPaid      = this.amount;
    }

    /** Marks payment as Failed with a reason note. */
    public void markFailed(String reason) {
        this.status = "Failed";
        this.notes  = reason;
    }

    /**
     * Marks payment as Refunded and records the refund amount.
     */
    public void refund() {
        refund(this.amountPaid);
    }

    public void refund(double refundAmt) {
        this.isRefunded  = true;
        this.refundAmount = refundAmt;
        this.refundDate  = LocalDateTime.now();
        this.status      = "Refunded";
    }

    /**
     * Returns a formatted invoice string for the linked booking.
     */
    public String generateInvoice() {
        String line = "=".repeat(50);
        return line + "\n"
                + "          HEAVEN HOTEL — INVOICE\n"
                + line + "\n"
                + "Payment ID  : " + paymentId + "\n"
                + "Booking ID  : " + bookingId + "\n"
                + "Amount      : LKR " + String.format("%,.2f", amount) + "\n"
                + "Method      : " + method + "\n"
                + "Status      : " + status + "\n"
                + "Date        : " + (transactionDate != null ? transactionDate.format(FMT) : paymentDate.format(FMT)) + "\n"
                + line;
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: paymentId|bookingId|amount|method|status|transactionDate
     */
    @Override
    public String toString() {
        String td = transactionDate != null ? transactionDate.format(FMT)
                : (paymentDate != null ? paymentDate.format(FMT) : "");
        return paymentId + "|" + bookingId + "|" + amount
                + "|" + (method != null ? method : "")
                + "|" + status + "|" + td;
    }

    public static Payment fromString(String line) {
        String[] f = line.split("\\|", -1);
        Payment p = new Payment();
        if (f.length > 0) p.setPaymentId(f[0]);
        if (f.length > 1) p.setBookingId(f[1]);
        if (f.length > 2) { try { p.setAmount(Double.parseDouble(f[2])); } catch (Exception ignored) {} }
        if (f.length > 3) p.setMethod(f[3]);
        if (f.length > 4) p.setStatus(f[4]);
        if (f.length > 5 && !f[5].isBlank()) {
            try { p.transactionDate = LocalDateTime.parse(f[5], FMT); }
            catch (Exception ignored) {}
        }
        return p;
    }

    // ── Status helpers ────────────────────────────────────────────────────────

    public boolean isPending()   { return "Pending".equalsIgnoreCase(status); }
    public boolean isCompleted() { return "Completed".equalsIgnoreCase(status); }
    public boolean isFailed()    { return "Failed".equalsIgnoreCase(status); }

    public double getRemainingAmount()  { return Math.max(0, amount - amountPaid); }
    public double getPaymentPercentage(){ return amount == 0 ? 100 : (amountPaid / amount) * 100; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getPaymentId()               { return paymentId; }
    public void   setPaymentId(String id)      { this.paymentId = id; }

    public String getBookingId()               { return bookingId; }
    public void   setBookingId(String id)      { this.bookingId = id; }

    public String getGuestId()                 { return guestId; }
    public void   setGuestId(String id)        { this.guestId = id; }

    public double getAmount()                  { return amount; }
    public void   setAmount(double a)          { this.amount = a; }

    public double getAmountPaid()              { return amountPaid; }
    public void   setAmountPaid(double a)      { this.amountPaid = a; }

    public String getCurrency()                { return currency; }
    public void   setCurrency(String c)        { this.currency = c; }

    public String getMethod()                  { return method; }
    public void   setMethod(String m)          { this.method = m; }
    public String getPaymentMethod()           { return method; }
    public void   setPaymentMethod(String m)   { this.method = m; }

    public String getStatus()                  { return status; }
    public void   setStatus(String s)          { this.status = s; }

    public LocalDateTime getPaymentDate()      { return paymentDate; }
    public void setPaymentDate(LocalDateTime v){ this.paymentDate = v; }

    public LocalDateTime getTransactionDate()  { return transactionDate; }
    public void setTransactionDate(LocalDateTime v){ this.transactionDate = v; }

    public String getTransactionId()           { return transactionId; }
    public void   setTransactionId(String id)  { this.transactionId = id; }

    public String getNotes()                   { return notes; }
    public void   setNotes(String n)           { this.notes = n; }

    public boolean isRefunded()                { return isRefunded; }
    public void    setRefunded(boolean v)      { this.isRefunded = v; }

    public double  getRefundAmount()           { return refundAmount; }
    public void    setRefundAmount(double v)   { this.refundAmount = v; }

    public LocalDateTime getRefundDate()       { return refundDate; }
    public void setRefundDate(LocalDateTime v) { this.refundDate = v; }
}
