package com.heaven.hotel.controller.booking;

import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.model.user.User;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.service.user.UserService;
import com.heaven.hotel.service.booking.PaymentService;
import com.heaven.hotel.service.booking.PricingService;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.room.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for booking operations
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private AvailabilityService availabilityService;
    
    @Autowired
    private PricingService pricingService;

    @Autowired
    private UserService userService;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Display booking search page
     */
    @GetMapping
    public String searchBookings(Model model) {
        model.addAttribute("pageTitle", "Search Bookings");
        model.addAttribute("availableRooms", roomService.getAvailableRooms());
        return "booking/search";
    }
    
    /**
     * Search available rooms
     */
    @PostMapping("/search")
    public String searchRooms(
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int numberOfGuests,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            LocalDate checkIn = LocalDate.parse(checkInDate, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutDate, dateFormatter);
            
            if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Check-out date must be after check-in date");
                return "redirect:/bookings";
            }
            
            int numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            List<Room> availableRooms = availabilityService.getAvailableRoomsForDateRange(checkIn, checkOut);
            
            model.addAttribute("availableRooms", availableRooms);
            model.addAttribute("checkInDate", checkInDate);
            model.addAttribute("checkOutDate", checkOutDate);
            model.addAttribute("numberOfNights", numberOfNights);
            model.addAttribute("numberOfGuests", numberOfGuests);
            model.addAttribute("pageTitle", "Available Rooms");
            
            return "booking/available-rooms";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid dates provided");
            return "redirect:/bookings";
        }
    }
    
    /**
     * Display booking form
     */
    @GetMapping("/room/{roomId}")
    public String bookRoom(
            @PathVariable String roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int numberOfGuests,
            Model model) {
        
        try {
            Room room = roomService.getRoomById(roomId);
            LocalDate checkIn = LocalDate.parse(checkInDate, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutDate, dateFormatter);
            int numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            
            double basePrice = pricingService.calculateBasePrice(room, numberOfNights);
            double taxAmount = pricingService.calculateTax(basePrice);
            double totalPrice = basePrice + taxAmount;
            
            model.addAttribute("room", room);
            model.addAttribute("checkInDate", checkInDate);
            model.addAttribute("checkOutDate", checkOutDate);
            model.addAttribute("numberOfNights", numberOfNights);
            model.addAttribute("numberOfGuests", numberOfGuests);
            model.addAttribute("basePrice", basePrice);
            model.addAttribute("taxAmount", taxAmount);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("pageTitle", "Book " + room.getRoomType() + " Room");
            
            return "booking/book-room";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Room not found");
            return "redirect:/bookings";
        }
    }
    
    /**
     * Create booking
     */
    @PostMapping("/create")
    public String createBooking(
            @RequestParam String roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int numberOfGuests,
            @RequestParam(required = false) String specialRequests,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            String guestId;
            try {
                User user = userService.getUserByUsername(auth.getName());
                guestId = user.getUserId();
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Could not resolve your account. Please log in again.");
                return "redirect:/bookings";
            }

            LocalDate checkIn = LocalDate.parse(checkInDate, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutDate, dateFormatter);

            Booking booking = bookingService.createBooking(
                guestId, roomId, checkIn, checkOut, numberOfGuests, specialRequests
            );
            
            // Auto-confirm and process payment
            bookingService.confirmBooking(booking.getBookingId());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Booking created successfully! Booking ID: " + booking.getBookingId());
            
            return "redirect:/bookings/" + booking.getBookingId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to create booking: " + e.getMessage());
            return "redirect:/bookings";
        }
    }
    
    /**
     * View booking details
     */
    @GetMapping("/{bookingId}")
    public String viewBooking(@PathVariable String bookingId, Model model) {
        try {
            Booking booking = bookingService.getBookingById(bookingId);
            Room room = roomService.getRoomById(booking.getRoomId());
            
            model.addAttribute("booking", booking);
            model.addAttribute("room", room);
            model.addAttribute("payments", paymentService.getBookingPayments(bookingId));
            model.addAttribute("pageTitle", "Booking Details");
            
            return "booking/view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Booking not found");
            return "redirect:/bookings";
        }
    }
    
    /**
     * List all bookings
     */
    @GetMapping("/list/all")
    public String listAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("pageTitle", "All Bookings");
        return "booking/list";
    }
    
    /**
     * Cancel booking
     */
    @PostMapping("/{bookingId}/cancel")
    public String cancelBooking(
            @PathVariable String bookingId,
            @RequestParam(required = false) String reason,
            RedirectAttributes redirectAttributes) {
        
        try {
            bookingService.cancelBooking(bookingId, reason != null ? reason : "Cancelled by user");
            redirectAttributes.addFlashAttribute("successMessage", 
                "Booking cancelled successfully");
            return "redirect:/bookings/" + bookingId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to cancel booking: " + e.getMessage());
            return "redirect:/bookings/" + bookingId;
        }
    }
    
    /**
     * Check in
     */
    @PostMapping("/{bookingId}/checkin")
    public String checkIn(@PathVariable String bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.checkIn(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Guest checked in successfully");
            return "redirect:/bookings/" + bookingId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bookings/" + bookingId;
        }
    }
    
    /**
     * Check out
     */
    @PostMapping("/{bookingId}/checkout")
    public String checkOut(@PathVariable String bookingId, RedirectAttributes redirectAttributes) {
        try {
            bookingService.checkOut(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "Guest checked out successfully");
            return "redirect:/bookings/" + bookingId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bookings/" + bookingId;
        }
    }
}
