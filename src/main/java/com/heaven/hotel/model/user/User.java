package com.heaven.hotel.model.user;

import java.time.LocalDateTime;

/**
 * Abstract base class for all user types.
 * OOP: Abstraction (abstract methods), Encapsulation (private fields + getters/setters)
 */
public abstract class User {

    protected String userId;
    protected String username;
    protected String password;   // hashed password
    protected String email;
    protected String contactNo;
    protected String firstName;
    protected String lastName;
    protected String role;       // "GUEST", "STAFF", "ADMIN"
    protected String status;     // "ACTIVE", "SUSPENDED"
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLogin;
    protected LocalDateTime updatedAt;

    public User() {}

    public User(String userId, String username, String password,
                String email, String contactNo, String role, String status) {
        this.userId    = userId;
        this.username  = username;
        this.password  = password;
        this.email     = email;
        this.contactNo = contactNo;
        this.role      = role;
        this.status    = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /** Convenience constructor for registration flows */
    public User(String username, String email, String contactNo,
                String firstName, String lastName, String role) {
        this.username  = username;
        this.email     = email;
        this.contactNo = contactNo;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.role      = role;
        this.status    = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ── Abstract methods (Polymorphism) ───────────────────────────────────────

    /** Polymorphic: each role implements its own authentication logic */
    public abstract boolean login(String username, String password);

    public abstract String getDisplayName();
    public abstract String getDefaultDashboardPage();
    public abstract boolean hasPermission(String permissionCode);

    /** Pipe-delimited string for file storage */
    @Override
    public abstract String toString();

    /** Factory method — routes to the correct subclass parser */
    public static User fromString(String line) {
        if (line == null || line.isBlank()) return null;
        String[] f = line.split("\\|", -1);
        if (f.length < 7) return null;
        switch (f[5].toUpperCase()) {
            case "GUEST": return GuestUser.fromString(line);
            case "STAFF": return StaffUser.fromString(line);
            case "ADMIN": return AdminUser.fromString(line);
            default:      return null;
        }
    }

    // ── Getters & Setters (Encapsulation) ────────────────────────────────────

    public String getUserId()                      { return userId; }
    public void   setUserId(String userId)         { this.userId = userId; }

    public String getUsername()                    { return username; }
    public void   setUsername(String u)            { this.username = u; }

    public String getPassword()                    { return password; }
    public void   setPassword(String p)            { this.password = p; }
    public String getPasswordHash()                { return password; }
    public void   setPasswordHash(String h)        { this.password = h; }

    public String getEmail()                       { return email; }
    public void   setEmail(String e)               { this.email = e; }

    public String getContactNo()                   { return contactNo; }
    public void   setContactNo(String c)           { this.contactNo = c; }
    public String getPhone()                       { return contactNo; }
    public void   setPhone(String p)               { this.contactNo = p; }

    public String getFirstName()                   { return firstName; }
    public void   setFirstName(String n)           { this.firstName = n; }

    public String getLastName()                    { return lastName; }
    public void   setLastName(String n)            { this.lastName = n; }

    public String getRole()                        { return role; }
    public void   setRole(String r)                { this.role = r; }

    public String getStatus()                      { return status; }
    public void   setStatus(String s)              { this.status = s; }

    public LocalDateTime getCreatedAt()            { return createdAt; }
    public void setCreatedAt(LocalDateTime v)      { this.createdAt = v; }

    public LocalDateTime getLastLogin()            { return lastLogin; }
    public void setLastLogin(LocalDateTime v)      { this.lastLogin = v; }

    public LocalDateTime getUpdatedAt()            { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v)      { this.updatedAt = v; }

    // ── Utility helpers ───────────────────────────────────────────────────────

    public boolean isActive()    { return "ACTIVE".equalsIgnoreCase(status); }
    public boolean isSuspended() { return "SUSPENDED".equalsIgnoreCase(status); }
    public boolean isAdmin()     { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isStaff()     { return "STAFF".equalsIgnoreCase(role); }
    public boolean isGuest()     { return "GUEST".equalsIgnoreCase(role); }

    public String getFullName() {
        String fn = firstName != null ? firstName : "";
        String ln = lastName  != null ? lastName  : "";
        return (fn + " " + ln).trim();
    }
}
