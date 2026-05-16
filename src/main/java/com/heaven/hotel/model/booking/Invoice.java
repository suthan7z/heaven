package com.heaven.hotel.model.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice model for billing
 */
public class Invoice {
    
    private String invoiceId;
    private String bookingId;
    private String guestId;
    private double subtotal;
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private String status; // DRAFT, ISSUED, PAID, OVERDUE
    private LocalDateTime issuedDate;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private String notes;
    private List<InvoiceItem> items;
    
    public Invoice() {
        this.items = new ArrayList<>();
    }
    
    public Invoice(String invoiceId, String bookingId, String guestId) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.status = "DRAFT";
        this.issuedDate = LocalDateTime.now();
        this.items = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }
    
    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDateTime getPaidDate() {
        return paidDate;
    }
    
    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
    
    // Add item to invoice
    public void addItem(InvoiceItem item) {
        this.items.add(item);
    }
    
    // Calculate totals
    public void calculateTotals() {
        this.subtotal = items.stream().mapToDouble(InvoiceItem::getTotal).sum();
    }
    
    // Status helpers
    public boolean isDraft() {
        return "DRAFT".equals(this.status);
    }
    
    public void issue() {
        this.status = "ISSUED";
        this.issuedDate = LocalDateTime.now();
    }
    
    public void markPaid() {
        this.status = "PAID";
        this.paidDate = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
    
    /**
     * Inner class for invoice line items
     */
    public static class InvoiceItem {
        private String description;
        private int quantity;
        private double unitPrice;
        private double total;
        
        public InvoiceItem(String description, int quantity, double unitPrice) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.total = quantity * unitPrice;
        }
        
        public String getDescription() { return description; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getTotal() { return total; }
    }
}
