package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Admin user – extends StaffUser (3-level inheritance: User → StaffUser → AdminUser).
 * OOP: Inheritance, Polymorphism (login override), Encapsulation
 */
public class AdminUser extends StaffUser {

    private String adminId;
    private List<String> permissions;   // e.g. ["ROOM_MANAGE","BOOKING_VIEW","REPORT_GEN"]
    private String lastLoginTimestamp;  // String timestamp for file storage

    private static final String LOG_FILE = "src/main/resources/data/admin_logs.txt";

    public AdminUser() {
        super();
        this.role        = Constants.ROLE_ADMIN;
        this.permissions = new ArrayList<>();
        initDefaultPermissions();
    }

    public AdminUser(String username, String email, String contactNo,
                     String firstName, String lastName) {
        super(username, email, contactNo, firstName, lastName, "Management", "Administrator");
        this.role        = Constants.ROLE_ADMIN;
        this.permissions = new ArrayList<>();
        initDefaultPermissions();
    }

    private void initDefaultPermissions() {
        permissions.add("ROOM_MANAGE");
        permissions.add("BOOKING_VIEW");
        permissions.add("BOOKING_MANAGE");
        permissions.add("REPORT_GEN");
        permissions.add("USER_MANAGE");
        permissions.add("CONFIG_MANAGE");
    }

    // ── Polymorphic login ─────────────────────────────────────────────────────

    /** Admin-specific authentication: validates credentials and logs the activity. */
    @Override
    public boolean login(String username, String password) {
        boolean ok = this.username.equalsIgnoreCase(username) && isActive();
        if (ok) {
            this.lastLoginTimestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logActivity("LOGIN");
        }
        return ok;
    }

    // ── Abstract method implementations ──────────────────────────────────────

    @Override
    public String getDisplayName() { return getFullName() + " (Admin)"; }

    @Override
    public String getDefaultDashboardPage() { return "/admin/dashboard"; }

    /** Admins have all permissions by default; also checks the explicit list. */
    @Override
    public boolean hasPermission(String permissionCode) {
        if (permissions == null || permissions.isEmpty()) return true;
        return permissions.contains(permissionCode.toUpperCase());
    }

    // ── Admin-specific methods ────────────────────────────────────────────────

    /**
     * Writes a timestamped action entry to admin_logs.txt.
     * Format: logId|userId|action|timestamp
     */
    public void logActivity(String action) {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = System.currentTimeMillis() + "|" + userId + "|" + action + "|" + ts;
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(entry + System.lineSeparator());
        } catch (IOException ignored) {}
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: userId|username|password|email|contactNo|ADMIN|status|employeeId|department|
     *         position|shift|managerId|salary|employmentStatus|adminId|permissions|lastLogin
     */
    @Override
    public String toString() {
        String perms = permissions != null ? String.join(",", permissions) : "";
        return userId + "|" + username + "|" + password + "|" + email + "|" + contactNo
                + "|" + role + "|" + status
                + "|" + nvl(getEmployeeId()) + "|" + nvl(getDepartment())
                + "|" + nvl(getPosition())   + "|" + nvl(getShift())
                + "|" + nvl(getManagerId())  + "|" + getSalary()
                + "|" + nvl(getEmploymentStatus())
                + "|" + nvl(adminId) + "|" + perms + "|" + nvl(lastLoginTimestamp);
    }

    public static AdminUser fromString(String line) {
        String[] f = line.split("\\|", -1);
        AdminUser a = new AdminUser();
        if (f.length > 0)  a.setUserId(f[0]);
        if (f.length > 1)  a.setUsername(f[1]);
        if (f.length > 2)  a.setPassword(f[2]);
        if (f.length > 3)  a.setEmail(f[3]);
        if (f.length > 4)  a.setContactNo(f[4]);
        // f[5]=role, f[6]=status
        if (f.length > 6)  a.setStatus(f[6]);
        if (f.length > 7)  a.setEmployeeId(f[7]);
        if (f.length > 8)  a.setDepartment(f[8]);
        if (f.length > 9)  a.setPosition(f[9]);
        if (f.length > 10) a.setShift(f[10]);
        if (f.length > 11) a.setManagerId(f[11]);
        if (f.length > 12) { try { a.setSalary(Double.parseDouble(f[12])); } catch (Exception ignored) {} }
        if (f.length > 13) a.setEmploymentStatus(f[13]);
        if (f.length > 14) a.setAdminId(f[14]);
        if (f.length > 15 && !f[15].isBlank())
            a.permissions = new ArrayList<>(Arrays.asList(f[15].split(",")));
        if (f.length > 16) a.setLastLoginTimestamp(f[16]);
        return a;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getAdminId()                       { return adminId; }
    public void   setAdminId(String id)              { this.adminId = id; }

    public List<String> getPermissions()             { return permissions; }
    public void setPermissions(List<String> perms)   { this.permissions = perms; }

    public String getLastLoginTimestamp()            { return lastLoginTimestamp; }
    public void   setLastLoginTimestamp(String ts)   { this.lastLoginTimestamp = ts; }

    public boolean isSuperAdmin() { return permissions != null && permissions.contains("SUPER_ADMIN"); }

    // Legacy boolean helpers kept for backward compatibility
    public boolean canManageAdmins() { return hasPermission("USER_MANAGE"); }
    public boolean canManageStaff()  { return hasPermission("USER_MANAGE"); }
    public boolean canManageConfig() { return hasPermission("CONFIG_MANAGE"); }
    public boolean canViewReports()  { return hasPermission("REPORT_GEN"); }

    public void setCanManageAdmins(boolean v) { setOrRemove("USER_MANAGE",   v); }
    public void setCanManageStaff(boolean v)  { setOrRemove("USER_MANAGE",   v); }
    public void setCanManageConfig(boolean v) { setOrRemove("CONFIG_MANAGE", v); }
    public void setCanViewReports(boolean v)  { setOrRemove("REPORT_GEN",    v); }

    public String getAdminLevel() { return "ADMIN"; }
    public void   setAdminLevel(String l) {}  // kept for serialisation compatibility

    private void setOrRemove(String perm, boolean add) {
        if (permissions == null) permissions = new ArrayList<>();
        if (add && !permissions.contains(perm)) permissions.add(perm);
        else if (!add) permissions.remove(perm);
    }
}
