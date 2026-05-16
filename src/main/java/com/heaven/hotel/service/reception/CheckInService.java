package com.heaven.hotel.service.reception;

import com.heaven.hotel.model.reception.CheckIn;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckInService {

    @Autowired
    private BookingService bookingService;

    private final List<CheckIn> checkIns = new ArrayList<>();

    public CheckIn processCheckIn(String bookingId, String staffId) {
        bookingService.checkIn(bookingId);
        var booking = bookingService.getBookingById(bookingId);
        CheckIn checkIn = new CheckIn(IdGenerator.generateId("CI"), bookingId,
                booking.getGuestId(), booking.getRoomId(), staffId);
        checkIns.add(checkIn);
        return checkIn;
    }

    public List<CheckIn> getAllCheckIns() { return new ArrayList<>(checkIns); }
}
