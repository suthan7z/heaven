package com.heaven.hotel.service.review;

import com.heaven.hotel.model.review.LoyaltyProgram;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoyaltyService {

    private final List<LoyaltyProgram> programs = new ArrayList<>();

    public LoyaltyProgram enroll(String guestId) {
        if (findByGuestId(guestId).isPresent()) return findByGuestId(guestId).get();
        LoyaltyProgram lp = new LoyaltyProgram(guestId, 0, "Bronze");
        programs.add(lp);
        return lp;
    }

    public Optional<LoyaltyProgram> findByGuestId(String guestId) {
        return programs.stream().filter(p -> p.getGuestId().equals(guestId)).findFirst();
    }

    public void addPoints(String guestId, long points) {
        findByGuestId(guestId).ifPresent(p -> p.addPoints(points));
    }

    public long getPoints(String guestId) {
        return findByGuestId(guestId).map(LoyaltyProgram::getTotalPoints).orElse(0L);
    }

    public List<LoyaltyProgram> getAllPrograms() { return new ArrayList<>(programs); }
}
