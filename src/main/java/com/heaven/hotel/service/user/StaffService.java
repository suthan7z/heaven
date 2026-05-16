package com.heaven.hotel.service.user;

import com.heaven.hotel.model.user.StaffUser;
import com.heaven.hotel.model.user.User;
import com.heaven.hotel.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private UserRepository userRepository;

    public List<StaffUser> getAllStaff() {
        return userRepository.findByRole("STAFF").stream()
                .filter(u -> u instanceof StaffUser)
                .map(u -> (StaffUser) u)
                .collect(Collectors.toList());
    }

    public StaffUser getStaffById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + userId));
        if (!(user instanceof StaffUser)) throw new RuntimeException("User is not a staff member");
        return (StaffUser) user;
    }

    public int getStaffCount() {
        return userRepository.getCountByRole("STAFF");
    }
}
