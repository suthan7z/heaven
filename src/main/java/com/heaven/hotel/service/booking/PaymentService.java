package com.heaven.hotel.service.booking;

import com.heaven.hotel.model.booking.Payment;
import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.repository.booking.PaymentRepository;
import com.heaven.hotel.utils.IdGenerator;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for payment operations
 */
@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * Create a new payment
     */
    public Payment createPayment(String bookingId, String guestId, double amount) {
        if (ValidationUtil.isEmpty(bookingId) || ValidationUtil.isEmpty(guestId)) {
            throw new IllegalArgumentException("Booking ID and Guest ID are required");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        
        Payment payment = new Payment(
            IdGenerator.generateId("PAY"),
            bookingId,
            guestId,
            amount
        );
        
        if (paymentRepository.save(payment)) {
            return payment;
        }
        
        throw new RuntimeException("Failed to create payment");
    }
    
    /**
     * Get payment by ID
     */
    public Payment getPaymentById(String paymentId) {
        if (ValidationUtil.isEmpty(paymentId)) {
            throw new IllegalArgumentException("Payment ID cannot be empty");
        }
        
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
    }
    
    /**
     * Get payments for a booking
     */
    public List<Payment> getBookingPayments(String bookingId) {
        if (ValidationUtil.isEmpty(bookingId)) {
            throw new IllegalArgumentException("Booking ID cannot be empty");
        }
        
        return paymentRepository.findByBookingId(bookingId);
    }
    
    /**
     * Process a payment
     */
    public void processPayment(String paymentId, String paymentMethod, String transactionId) {
        Payment payment = getPaymentById(paymentId);
        
        if (!payment.isPending()) {
            throw new IllegalArgumentException("Payment is not in pending status");
        }
        
        payment.setPaymentMethod(paymentMethod);
        payment.markCompleted(transactionId);
        
        // Update booking payment status
        Booking booking = bookingService.getBookingById(payment.getBookingId());
        if (payment.getAmount() >= booking.getTotalPrice()) {
            booking.setPaymentStatus("PAID");
        } else {
            booking.setPaymentStatus("PARTIALLY_PAID");
        }
        
        paymentRepository.update(payment);
    }
    
    /**
     * Fail a payment
     */
    public void failPayment(String paymentId, String reason) {
        Payment payment = getPaymentById(paymentId);
        payment.markFailed(reason);
        paymentRepository.update(payment);
    }
    
    /**
     * Refund a payment
     */
    public void refundPayment(String paymentId, double refundAmount, String reason) {
        Payment payment = getPaymentById(paymentId);
        
        if (!payment.isCompleted()) {
            throw new IllegalArgumentException("Only completed payments can be refunded");
        }
        
        if (refundAmount <= 0 || refundAmount > payment.getAmountPaid()) {
            throw new IllegalArgumentException("Invalid refund amount");
        }
        
        payment.refund(refundAmount);
        payment.setNotes(reason);
        
        // Update booking payment status
        Booking booking = bookingService.getBookingById(payment.getBookingId());
        double remainingAmount = payment.getAmountPaid() - refundAmount;
        
        if (remainingAmount <= 0) {
            booking.setPaymentStatus("UNPAID");
        } else if (remainingAmount < booking.getTotalPrice()) {
            booking.setPaymentStatus("PARTIALLY_PAID");
        }
        
        paymentRepository.update(payment);
    }
    
    /**
     * Get total paid amount for a booking
     */
    public double getTotalPaidForBooking(String bookingId) {
        List<Payment> payments = getBookingPayments(bookingId);
        return payments.stream()
            .filter(Payment::isCompleted)
            .mapToDouble(Payment::getAmountPaid)
            .sum();
    }
    
    /**
     * Check if booking is fully paid
     */
    public boolean isBookingFullyPaid(String bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        double totalPaid = getTotalPaidForBooking(bookingId);
        return totalPaid >= booking.getTotalPrice();
    }
    
    /**
     * Get pending payments
     */
    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("PENDING");
    }
    
    /**
     * Get completed payments
     */
    public List<Payment> getCompletedPayments() {
        return paymentRepository.findByStatus("COMPLETED");
    }
    
    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
