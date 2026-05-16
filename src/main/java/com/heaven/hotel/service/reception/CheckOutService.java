package com.heaven.hotel.service.reception;

import com.heaven.hotel.model.reception.CheckOut;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckOutService {

    @Autowired
    private BookingService bookingService;

    private final List<CheckOut> checkOuts = new ArrayList<>();

    public CheckOut processCheckOut(String bookingId, String staffId) {
        bookingService.checkOut(bookingId);
        var booking = bookingService.getBookingById(bookingId);
        CheckOut checkOut = new CheckOut(IdGenerator.generateId("CO"), bookingId,
                booking.getGuestId(), booking.getRoomId(), staffId);
        checkOuts.add(checkOut);
        return checkOut;
    }

    public List<CheckOut> getAllCheckOuts() { return new ArrayList<>(checkOuts); }
}
