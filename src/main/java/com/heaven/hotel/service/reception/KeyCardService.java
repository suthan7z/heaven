package com.heaven.hotel.service.reception;

import com.heaven.hotel.model.reception.KeyCard;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KeyCardService {

    private final List<KeyCard> keyCards = new ArrayList<>();

    public KeyCard issueKeyCard(String bookingId, String guestId, String roomId) {
        KeyCard keyCard = new KeyCard(IdGenerator.generateKeycardId(), roomId, bookingId, guestId);
        keyCards.add(keyCard);
        return keyCard;
    }

    public void deactivateKeyCard(String keyCardId) {
        keyCards.stream().filter(k -> k.getKeyCardId().equals(keyCardId))
                .findFirst().ifPresent(k -> k.setStatus("DEACTIVATED"));
    }

    public Optional<KeyCard> findByBookingId(String bookingId) {
        return keyCards.stream().filter(k -> k.getBookingId().equals(bookingId)).findFirst();
    }

    public List<KeyCard> getAllKeyCards() { return new ArrayList<>(keyCards); }
}
