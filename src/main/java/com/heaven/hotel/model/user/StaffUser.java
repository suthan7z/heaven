package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;

/**
 * Staff user – handles reception, room management, and maintenance.
 * OOP: Inheritance (extends User), Polymorphism (login override)
 */
public class StaffUser extends User {

    private String employeeId;
    private String department;
    private String position;
    private String shift;           // "Morning", "Evening", "Night"
    private String managerId;
    private double salary;
    private String employmentStatus; // "ACTIVE", "INACTIVE"

    public StaffUser() {
        super();
        this.role             = Constants.ROLE_STAFF;
        this.employmentStatus = "ACTIVE";
    }

    public StaffUser(String username, String email, String contactNo,
                     String firstName, String lastName,
                     String department, String position) {
        super(username, email, contactNo, firstName, lastName, Constants.ROLE_STAFF);
        this.department       = department;
        this.position         = position;
        this.employmentStatus = "ACTIVE";
    }

    // ── Polymorphic login ─────────────────────────────────────────────────────

    /**
     * Staff-specific authentication: account must be ACTIVE and employment ACTIVE.
     */
    @Override
    public boolean login(String username, String password) {
        return this.username.equalsIgnoreCase(username)
                && isActive()
                && "ACTIVE".equalsIgnoreCase(employmentStatus);
    }

    // ── Abstract method implementations ──────────────────────────────────────

    @Override
    public String getDisplayName() {
        return getFullName() + " (" + (position != null ? position : "Staff") + ")";
    }

    @Override
    public String getDefaultDashboardPage() { return "/dashboard"; }

    @Override
    public boolean hasPermission(String permissionCode) {
        switch (permissionCode) {
            case "view_rooms":    case "manage_rooms":  case "check_in":
            case "check_out":     case "issue_keycard": case "view_booking":
            case "manage_booking": case "add_charges":  case "view_profile":
            case "edit_profile":  case "view_guests":   case "manage_maintenance":
                return true;
            default: return false;
        }
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: userId|username|password|email|contactNo|STAFF|status|employeeId|department|position|shift|managerId|salary|employmentStatus
     */
    @Override
    public String toString() {
        return userId + "|" + username + "|" + password + "|" + email + "|" + contactNo
                + "|" + role + "|" + status
                + "|" + nvl(employeeId) + "|" + nvl(department) + "|" + nvl(position)
                + "|" + nvl(shift) + "|" + nvl(managerId) + "|" + salary
                + "|" + nvl(employmentStatus);
    }

    public static StaffUser fromString(String line) {
        String[] f = line.split("\\|", -1);
        StaffUser s = new StaffUser();
        if (f.length > 0) s.setUserId(f[0]);
        if (f.length > 1) s.setUsername(f[1]);
        if (f.length > 2) s.setPassword(f[2]);
        if (f.length > 3) s.setEmail(f[3]);
        if (f.length > 4) s.setContactNo(f[4]);
        // f[5]=role, f[6]=status
        if (f.length > 6)  s.setStatus(f[6]);
        if (f.length > 7)  s.setEmployeeId(f[7]);
        if (f.length > 8)  s.setDepartment(f[8]);
        if (f.length > 9)  s.setPosition(f[9]);
        if (f.length > 10) s.setShift(f[10]);
        if (f.length > 11) s.setManagerId(f[11]);
        if (f.length > 12) { try { s.setSalary(Double.parseDouble(f[12])); } catch (Exception ignored) {} }
        if (f.length > 13) s.setEmploymentStatus(f[13]);
        return s;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getEmployeeId()                { return employeeId; }
    public void   setEmployeeId(String id)       { this.employeeId = id; }

    public String getDepartment()                { return department; }
    public void   setDepartment(String d)        { this.department = d; }

    public String getPosition()                  { return position; }
    public void   setPosition(String p)          { this.position = p; }

    public String getShift()                     { return shift; }
    public void   setShift(String s)             { this.shift = s; }

    public String getManagerId()                 { return managerId; }
    public void   setManagerId(String id)        { this.managerId = id; }

    public double getSalary()                    { return salary; }
    public void   setSalary(double salary)       { this.salary = salary; }

    public String getEmploymentStatus()          { return employmentStatus; }
    public void   setEmploymentStatus(String es) { this.employmentStatus = es; }

    public boolean isEmployed() { return "ACTIVE".equalsIgnoreCase(employmentStatus); }
}
