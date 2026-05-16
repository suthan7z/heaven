package com.heaven.hotel.repository.reception;

import com.heaven.hotel.model.reception.KeyCard;
import java.util.List;
import java.util.Optional;

public interface KeyCardRepository {
    boolean save(KeyCard keyCard);
    boolean update(KeyCard keyCard);
    Optional<KeyCard> findById(String keyCardId);
    List<KeyCard> findByBookingId(String bookingId);
    List<KeyCard> findByGuestId(String guestId);
    List<KeyCard> findAll();
}
