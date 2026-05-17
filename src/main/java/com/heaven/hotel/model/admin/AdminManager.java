package com.heaven.hotel.model.admin;

import com.heaven.hotel.model.user.AdminUser;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminManager — manages admin accounts and logs activity.
 * OOP: Encapsulation (admin data managed through controlled methods)
 *
 * Design: Uses composition (has a List<AdminUser>) rather than inheritance.
 * Receives the full user list from the service layer and filters by role.
 */
public class AdminManager {

    private List<AdminUser> admins;
    private String adminLogFile;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdminManager() {
        this.admins       = new ArrayList<>();
        this.adminLogFile = "src/main/resources/data/admin_logs.txt";
    }

    public AdminManager(List<AdminUser> admins) {
        this.admins       = admins != null ? admins : new ArrayList<>();
        this.adminLogFile = "src/main/resources/data/admin_logs.txt";
    }

    // ── Admin CRUD ────────────────────────────────────────────────────────────

    public List<AdminUser> getAllAdmins() {
        return new ArrayList<>(admins);
    }

    public AdminUser getAdminByUserId(String userId) {
        return admins.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst().orElse(null);
    }

    public AdminUser getAdminByAdminId(String adminId) {
        return admins.stream()
                .filter(a -> adminId.equals(a.getAdminId()))
                .findFirst().orElse(null);
    }

    public boolean addAdmin(AdminUser admin) {
        if (admin == null) return false;
        admins.add(admin);
        logAdminActivity("SYSTEM", "Admin added: " + admin.getUsername());
        return true;
    }

    public boolean updateAdmin(AdminUser updated) {
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUserId().equals(updated.getUserId())) {
                admins.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAdmin(String userId) {
        boolean removed = admins.removeIf(a -> a.getUserId().equals(userId));
        if (removed) logAdminActivity("SYSTEM", "Admin removed: " + userId);
        return removed;
    }

    public void updateAdminPermissions(String userId, List<String> permissions) {
        AdminUser admin = getAdminByUserId(userId);
        if (admin != null) {
            admin.setPermissions(permissions);
            logAdminActivity(userId, "Permissions updated: " + permissions);
        }
    }

    // ── Activity logging ──────────────────────────────────────────────────────

    /**
     * Appends a timestamped action entry to admin_logs.txt.
     * Format: logId|userId|action|timestamp
     */
    public void logAdminActivity(String userId, String action) {
        String ts    = LocalDateTime.now().format(FMT);
        String logId = "LOG" + System.currentTimeMillis();
        String entry = logId + "|" + userId + "|" + action + "|" + ts;
        try (FileWriter fw = new FileWriter(adminLogFile, true)) {
            fw.write(entry + System.lineSeparator());
        } catch (IOException ignored) {}
    }

    public List<String> getAdminLogs(String userId) {
        // Returns in-memory placeholder; full implementation reads admin_logs.txt
        return new ArrayList<>();
    }

    public boolean hasPermission(String userId, String permission) {
        AdminUser admin = getAdminByUserId(userId);
        return admin != null && admin.hasPermission(permission);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getAdminLogFile()               { return adminLogFile; }
    public void   setAdminLogFile(String path)    { this.adminLogFile = path; }
}
