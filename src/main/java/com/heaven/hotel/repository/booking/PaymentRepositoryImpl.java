package com.heaven.hotel.repository.booking;

import com.heaven.hotel.model.booking.Payment;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of PaymentRepository
 */
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    
    private static final List<Payment> payments = new ArrayList<>();
    
    @Override
    public boolean save(Payment payment) {
        return payments.add(payment);
    }
    
    @Override
    public boolean update(Payment payment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(payment.getPaymentId())) {
                payments.set(i, payment);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean delete(String paymentId) {
        return payments.removeIf(p -> p.getPaymentId().equals(paymentId));
    }
    
    @Override
    public Optional<Payment> findById(String paymentId) {
        return payments.stream()
            .filter(p -> p.getPaymentId().equals(paymentId))
            .findFirst();
    }
    
    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
    
    @Override
    public List<Payment> findByBookingId(String bookingId) {
        return payments.stream()
            .filter(p -> p.getBookingId().equals(bookingId))
            .toList();
    }
    
    @Override
    public List<Payment> findByGuestId(String guestId) {
        return payments.stream()
            .filter(p -> p.getGuestId().equals(guestId))
            .toList();
    }
    
    @Override
    public List<Payment> findByStatus(String status) {
        return payments.stream()
            .filter(p -> p.getStatus().equals(status))
            .toList();
    }
}
