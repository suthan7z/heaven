package com.heaven.hotel.repository.admin;

import com.heaven.hotel.model.user.AdminUser;
import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    boolean save(AdminUser admin);
    boolean update(AdminUser admin);
    boolean delete(String adminId);
    Optional<AdminUser> findById(String adminId);
    List<AdminUser> findAll();
}
