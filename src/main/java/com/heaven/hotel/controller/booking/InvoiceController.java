package com.heaven.hotel.controller.booking;

import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.model.booking.Payment;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.service.booking.InvoiceService;
import com.heaven.hotel.service.booking.PaymentService;
import com.heaven.hotel.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired private BookingService bookingService;
    @Autowired private PaymentService paymentService;
    @Autowired private RoomService    roomService;
    @Autowired private InvoiceService invoiceService;

    /** Invoice for a specific booking */
    @GetMapping("/booking/{bookingId}")
    public String viewInvoice(@PathVariable String bookingId, Model model) {
        try {
            Booking       booking  = bookingService.getBookingById(bookingId);
            Room          room     = roomService.getRoomById(booking.getRoomId());
            List<Payment> payments = paymentService.getBookingPayments(bookingId);

            double totalPaid = payments.stream()
                    .filter(p -> "Completed".equalsIgnoreCase(p.getStatus()))
                    .mapToDouble(Payment::getAmount).sum();
            double balance = booking.getTotalPrice() - totalPaid;

            model.addAttribute("booking",   booking);
            model.addAttribute("room",      room);
            model.addAttribute("payments",  payments);
            model.addAttribute("totalPaid", totalPaid);
            model.addAttribute("balance",   balance);
            model.addAttribute("invoice",   invoiceService.generateInvoice(booking, room));
            model.addAttribute("pageTitle", "Invoice — " + bookingId);
            return "booking/invoice";
        } catch (Exception e) {
            return "redirect:/bookings/list/all";
        }
    }

    /** List all invoices (stub) */
    @GetMapping
    public String listInvoices(Model model) {
        model.addAttribute("pageTitle", "Invoices");
        return "booking/invoices";
    }
}
