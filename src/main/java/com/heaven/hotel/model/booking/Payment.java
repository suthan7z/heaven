package com.heaven.hotel.model.booking;

import java.time.LocalDateTime;

/**
 * Payment model representing a payment transaction
 */
public class Payment {
    
    private String paymentId;
    private String bookingId;
    private String guestId;
    private double amount;
    private double amountPaid;
    private String currency;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, CASH
    private String status; // PENDING, COMPLETED, FAILED, REFUNDED
    private LocalDateTime paymentDate;
    private LocalDateTime transactionDate;
    private String transactionId;
    private String notes;
    private boolean isRefunded;
    private double refundAmount;
    private LocalDateTime refundDate;
    
    public Payment() {}
    
    public Payment(String paymentId, String bookingId, String guestId, double amount) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.amount = amount;
        this.amountPaid = 0;
        this.currency = "USD";
        this.status = "PENDING";
        this.paymentDate = LocalDateTime.now();
        this.isRefunded = false;
    }
    
    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
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
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isRefunded() {
        return isRefunded;
    }
    
    public void setRefunded(boolean refunded) {
        isRefunded = refunded;
    }
    
    public double getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    
    public LocalDateTime getRefundDate() {
        return refundDate;
    }
    
    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }
    
    // Status helpers
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }
    
    public boolean isFailed() {
        return "FAILED".equals(this.status);
    }
    
    // Payment operations
    public void markCompleted(String transactionId) {
        this.status = "COMPLETED";
        this.transactionDate = LocalDateTime.now();
        this.transactionId = transactionId;
        this.amountPaid = this.amount;
    }
    
    public void markFailed(String reason) {
        this.status = "FAILED";
        this.notes = reason;
    }
    
    public void refund(double refundAmount) {
        this.isRefunded = true;
        this.refundAmount = refundAmount;
        this.refundDate = LocalDateTime.now();
        this.status = "REFUNDED";
    }
    
    public double getRemainingAmount() {
        return Math.max(0, this.amount - this.amountPaid);
    }
    
    public double getPaymentPercentage() {
        if (this.amount == 0) return 100;
        return (this.amountPaid / this.amount) * 100;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
