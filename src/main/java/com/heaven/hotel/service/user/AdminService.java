package com.heaven.hotel.service.user;

import com.heaven.hotel.model.user.AdminUser;
import com.heaven.hotel.model.user.User;
import com.heaven.hotel.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<AdminUser> getAllAdmins() {
        return userRepository.findByRole("ADMIN").stream()
                .filter(u -> u instanceof AdminUser)
                .map(u -> (AdminUser) u)
                .collect(Collectors.toList());
    }

    public AdminUser getAdminById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + userId));
        if (!(user instanceof AdminUser)) throw new RuntimeException("User is not an admin");
        return (AdminUser) user;
    }
}
