package com.heaven.hotel.service.user;

import com.heaven.hotel.model.user.GuestUser;
import com.heaven.hotel.model.user.User;
import com.heaven.hotel.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {

    @Autowired
    private UserRepository userRepository;

    public List<GuestUser> getAllGuests() {
        return userRepository.findByRole("GUEST").stream()
                .filter(u -> u instanceof GuestUser)
                .map(u -> (GuestUser) u)
                .collect(Collectors.toList());
    }

    public GuestUser getGuestById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Guest not found: " + userId));
        if (!(user instanceof GuestUser)) throw new RuntimeException("User is not a guest");
        return (GuestUser) user;
    }

    public int getGuestCount() {
        return userRepository.getCountByRole("GUEST");
    }
}
