package com.heaven.hotel.service.booking;

import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.repository.booking.BookingRepository;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.room.AvailabilityService;
import com.heaven.hotel.utils.IdGenerator;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for booking operations
 */
@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private AvailabilityService availabilityService;
    
    @Autowired
    private PricingService pricingService;
    
    /**
     * Create a new booking
     */
    public Booking createBooking(String guestId, String roomId, LocalDate checkInDate, 
                                 LocalDate checkOutDate, int numberOfGuests, String specialRequests) {
        
        // Validate inputs
        if (ValidationUtil.isEmpty(guestId) || ValidationUtil.isEmpty(roomId)) {
            throw new IllegalArgumentException("Guest ID and Room ID are required");
        }
        
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.equals(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        if (numberOfGuests <= 0) {
            throw new IllegalArgumentException("Number of guests must be at least 1");
        }
        
        // Check room availability
        if (!availabilityService.isRoomAvailable(roomId)) {
            throw new IllegalArgumentException("Room is not available");
        }
        
        // Create booking
        Booking booking = new Booking(
            IdGenerator.generateId("BOOK"),
            guestId,
            roomId,
            checkInDate,
            checkOutDate,
            numberOfGuests
        );
        
        booking.setSpecialRequests(specialRequests);
        
        // Calculate pricing
        Room room = roomService.getRoomById(roomId);
        double basePrice = pricingService.calculateBasePrice(room, booking.getNumberOfNights());
        double taxAmount = pricingService.calculateTax(basePrice);
        double totalPrice = basePrice + taxAmount;
        
        booking.setBasePrice(basePrice);
        booking.setTaxAmount(taxAmount);
        booking.setTotalPrice(totalPrice);
        
        // Save booking
        if (bookingRepository.save(booking)) {
            return booking;
        }
        
        throw new RuntimeException("Failed to create booking");
    }
    
    /**
     * Get booking by ID
     */
    public Booking getBookingById(String bookingId) {
        if (ValidationUtil.isEmpty(bookingId)) {
            throw new IllegalArgumentException("Booking ID cannot be empty");
        }
        
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
    }
    
    /**
     * Get all bookings for a guest
     */
    public List<Booking> getGuestBookings(String guestId) {
        if (ValidationUtil.isEmpty(guestId)) {
            throw new IllegalArgumentException("Guest ID cannot be empty");
        }
        
        return bookingRepository.findByGuestId(guestId);
    }
    
    /**
     * Get all bookings for a room
     */
    public List<Booking> getRoomBookings(String roomId) {
        if (ValidationUtil.isEmpty(roomId)) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        
        return bookingRepository.findByRoomId(roomId);
    }
    
    /**
     * Get all confirmed bookings
     */
    public List<Booking> getConfirmedBookings() {
        return bookingRepository.findByStatus("CONFIRMED");
    }
    
    /**
     * Get all pending bookings
     */
    public List<Booking> getPendingBookings() {
        return bookingRepository.findByStatus("PENDING");
    }
    
    /**
     * Confirm a booking
     */
    public void confirmBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        
        if (!booking.isPending()) {
            throw new IllegalArgumentException("Only pending bookings can be confirmed");
        }
        
        booking.confirm();
        Room room = roomService.getRoomById(booking.getRoomId());
        room.markReserved();
        
        bookingRepository.update(booking);
    }
    
    /**
     * Cancel a booking
     */
    public void cancelBooking(String bookingId, String reason) {
        Booking booking = getBookingById(bookingId);
        
        if (booking.isCancelled() || booking.isCheckedOut()) {
            throw new IllegalArgumentException("Cannot cancel completed or already cancelled bookings");
        }
        
        booking.cancel();
        booking.setNotes(reason);
        
        // Mark room as available if not already checked out
        if (!booking.isCheckedOut()) {
            Room room = roomService.getRoomById(booking.getRoomId());
            room.markAvailable();
        }
        
        bookingRepository.update(booking);
    }
    
    /**
     * Check in a booking
     */
    public void checkIn(String bookingId) {
        Booking booking = getBookingById(bookingId);
        
        if (!booking.isConfirmed()) {
            throw new IllegalArgumentException("Only confirmed bookings can be checked in");
        }
        
        booking.checkIn();
        Room room = roomService.getRoomById(booking.getRoomId());
        room.markOccupied();
        
        bookingRepository.update(booking);
    }
    
    /**
     * Check out a booking
     */
    public void checkOut(String bookingId) {
        Booking booking = getBookingById(bookingId);
        
        if (!booking.isCheckedIn()) {
            throw new IllegalArgumentException("Only checked-in bookings can be checked out");
        }
        
        booking.checkOut();
        Room room = roomService.getRoomById(booking.getRoomId());
        room.markAvailable();
        
        bookingRepository.update(booking);
    }
    
    /**
     * Apply discount to booking
     */
    public void applyDiscount(String bookingId, double discountAmount) {
        Booking booking = getBookingById(bookingId);
        
        if (discountAmount < 0 || discountAmount > booking.getTotalPrice()) {
            throw new IllegalArgumentException("Invalid discount amount");
        }
        
        booking.setDiscountAmount(discountAmount);
        booking.setTotalPrice(booking.getBasePrice() + booking.getTaxAmount() - discountAmount);
        
        bookingRepository.update(booking);
    }
    
    /**
     * Get bookings for a date range
     */
    public List<Booking> getBookingsForDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByDateRange(startDate, endDate);
    }
    
    /**
     * Get revenue for a date range
     */
    public double getRevenueForDateRange(LocalDate startDate, LocalDate endDate) {
        return getBookingsForDateRange(startDate, endDate).stream()
            .filter(Booking::isPaid)
            .mapToDouble(Booking::getTotalPrice)
            .sum();
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * Update booking
     */
    public void updateBooking(String bookingId, LocalDate newCheckIn, LocalDate newCheckOut) {
        Booking booking = getBookingById(bookingId);
        
        if (newCheckOut.isBefore(newCheckIn) || newCheckOut.equals(newCheckIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        booking.setCheckInDate(newCheckIn);
        booking.setCheckOutDate(newCheckOut);
        booking.setNumberOfNights((int) java.time.temporal.ChronoUnit.DAYS.between(newCheckIn, newCheckOut));
        
        // Recalculate pricing
        Room room = roomService.getRoomById(booking.getRoomId());
        double basePrice = pricingService.calculateBasePrice(room, booking.getNumberOfNights());
        double taxAmount = pricingService.calculateTax(basePrice);
        
        booking.setBasePrice(basePrice);
        booking.setTaxAmount(taxAmount);
        booking.setTotalPrice(basePrice + taxAmount - booking.getDiscountAmount());
        
        bookingRepository.update(booking);
    }
}
