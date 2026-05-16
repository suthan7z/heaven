package com.heaven.hotel.repository.booking;

import com.heaven.hotel.model.booking.Payment;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for payment operations
 */
public interface PaymentRepository {
    
    boolean save(Payment payment);
    boolean update(Payment payment);
    boolean delete(String paymentId);
    Optional<Payment> findById(String paymentId);
    List<Payment> findAll();
    List<Payment> findByBookingId(String bookingId);
    List<Payment> findByGuestId(String guestId);
    List<Payment> findByStatus(String status);
}
