package com.heaven.hotel.repository.booking;

import com.heaven.hotel.model.booking.Booking;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of BookingRepository
 */
@Repository
public class BookingRepositoryImpl implements BookingRepository {
    
    private static final List<Booking> bookings = new ArrayList<>();
    
    @Override
    public boolean save(Booking booking) {
        return bookings.add(booking);
    }
    
    @Override
    public boolean update(Booking booking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(booking.getBookingId())) {
                bookings.set(i, booking);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean delete(String bookingId) {
        return bookings.removeIf(b -> b.getBookingId().equals(bookingId));
    }
    
    @Override
    public Optional<Booking> findById(String bookingId) {
        return bookings.stream()
            .filter(b -> b.getBookingId().equals(bookingId))
            .findFirst();
    }
    
    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }
    
    @Override
    public List<Booking> findByGuestId(String guestId) {
        return bookings.stream()
            .filter(b -> b.getGuestId().equals(guestId))
            .toList();
    }
    
    @Override
    public List<Booking> findByRoomId(String roomId) {
        return bookings.stream()
            .filter(b -> b.getRoomId().equals(roomId))
            .toList();
    }
    
    @Override
    public List<Booking> findByStatus(String status) {
        return bookings.stream()
            .filter(b -> b.getStatus().equals(status))
            .toList();
    }
    
    @Override
    public List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookings.stream()
            .filter(b -> !b.getCheckInDate().isBefore(startDate) && 
                        !b.getCheckOutDate().isAfter(endDate))
            .toList();
    }
    
    @Override
    public int countByStatus(String status) {
        return (int) bookings.stream()
            .filter(b -> b.getStatus().equals(status))
            .count();
    }
}
