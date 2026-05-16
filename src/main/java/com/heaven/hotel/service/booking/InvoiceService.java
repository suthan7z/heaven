package com.heaven.hotel.service.booking;

import com.heaven.hotel.model.booking.Invoice;
import com.heaven.hotel.model.booking.Booking;
import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.stereotype.Service;

/**
 * Service for invoice operations
 */
@Service
public class InvoiceService {
    
    /**
     * Generate invoice from booking
     */
    public Invoice generateInvoice(Booking booking, Room room) {
        Invoice invoice = new Invoice(
            IdGenerator.generateId("INV"),
            booking.getBookingId(),
            booking.getGuestId()
        );
        
        // Add room charge line item
        invoice.addItem(new Invoice.InvoiceItem(
            "Room Charge - " + booking.getNumberOfNights() + " nights",
            booking.getNumberOfNights(),
            room.getPricePerNight()
        ));
        
        invoice.calculateTotals();
        invoice.setSubtotal(booking.getBasePrice());
        invoice.setTaxAmount(booking.getTaxAmount());
        invoice.setDiscountAmount(booking.getDiscountAmount());
        invoice.setTotalAmount(booking.getTotalPrice());
        invoice.issue();
        
        return invoice;
    }
}
