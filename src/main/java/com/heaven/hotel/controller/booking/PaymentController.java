package com.heaven.hotel.controller.booking;

import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.model.booking.Payment;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.service.booking.PaymentService;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private BookingService bookingService;
    @Autowired private RoomService    roomService;

    /** List all payments (admin/staff view) */
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("pageTitle", "Payments");
        return "booking/payments";
    }

    /** Payment form for a specific booking */
    @GetMapping("/booking/{bookingId}")
    public String paymentForm(@PathVariable String bookingId, Model model) {
        try {
            Booking booking = bookingService.getBookingById(bookingId);
            Room    room    = roomService.getRoomById(booking.getRoomId());
            List<Payment> existing = paymentService.getBookingPayments(bookingId);

            model.addAttribute("booking",  booking);
            model.addAttribute("room",     room);
            model.addAttribute("payments", existing);
            model.addAttribute("pageTitle", "Pay for Booking");
            return "booking/payment-form";
        } catch (Exception e) {
            return "redirect:/bookings/list/all";
        }
    }

    /** Process a payment */
    @PostMapping("/booking/{bookingId}/pay")
    public String processPayment(
            @PathVariable String bookingId,
            @RequestParam String paymentMethod,
            Authentication auth,
            RedirectAttributes ra) {
        try {
            Booking booking = bookingService.getBookingById(bookingId);
            String guestId = booking.getGuestId();

            Payment payment = paymentService.createPayment(bookingId, guestId, booking.getTotalPrice());
            payment.setMethod(paymentMethod);
            paymentService.processPayment(payment.getPaymentId(),
                    paymentMethod, IdGenerator.generateTransactionRef());

            booking.setPaymentStatus("PAID");
            ra.addFlashAttribute("successMessage", "Payment successful!");
            return "redirect:/invoices/booking/" + bookingId;
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Payment failed: " + e.getMessage());
            return "redirect:/payments/booking/" + bookingId;
        }
    }
}
