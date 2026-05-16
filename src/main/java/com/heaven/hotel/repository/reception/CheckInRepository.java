package com.heaven.hotel.repository.reception;

import com.heaven.hotel.model.reception.CheckIn;
import java.util.List;
import java.util.Optional;

public interface CheckInRepository {
    boolean save(CheckIn checkIn);
    Optional<CheckIn> findById(String checkInId);
    List<CheckIn> findByBookingId(String bookingId);
    List<CheckIn> findAll();
}
