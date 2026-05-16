package com.heaven.hotel.repository.booking;

import com.heaven.hotel.model.booking.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for booking operations
 */
public interface BookingRepository {
    
    boolean save(Booking booking);
    boolean update(Booking booking);
    boolean delete(String bookingId);
    Optional<Booking> findById(String bookingId);
    List<Booking> findAll();
    List<Booking> findByGuestId(String guestId);
    List<Booking> findByRoomId(String roomId);
    List<Booking> findByStatus(String status);
    List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate);
    int countByStatus(String status);
}
