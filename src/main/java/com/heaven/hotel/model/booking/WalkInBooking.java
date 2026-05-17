package com.heaven.hotel.model.booking;

import java.time.LocalDate;

/**
 * Walk-in booking – guest arrives and books on the spot without a prior reservation.
 * OOP: Inheritance (extends Booking), Polymorphism (confirm overrides standard flow)
 */
public class WalkInBooking extends Booking {

    private String arrivalMode; // "Walk-In"

    public WalkInBooking() {
        super();
        this.arrivalMode = "Walk-In";
    }

    public WalkInBooking(String bookingId, String guestId, String roomId,
                         LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        super(bookingId, guestId, roomId, checkInDate, checkOutDate, numberOfGuests);
        this.arrivalMode = "Walk-In";
    }

    /**
     * Walk-in guests are confirmed AND checked-in immediately.
     * Overrides the standard confirm() which only sets status to Confirmed.
     */
    @Override
    public void confirm() {
        super.confirm();
        checkIn(); // immediately mark as checked-in
        setSpecialRequests("Walk-in — no prior reservation");
    }

    public String getArrivalMode() { return arrivalMode; }
    public void   setArrivalMode(String m) { this.arrivalMode = m; }

    @Override
    public String toString() {
        return "WALKIN|" + super.toString();
    }
}
