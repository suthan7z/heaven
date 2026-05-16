package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;

/**
 * Admin user - full system access and management
 */
public class AdminUser extends User {
    
    private String adminLevel;
    private String department;
    private String permissions;
    private boolean canManageAdmins;
    private boolean canManageStaff;
    private boolean canManageConfig;
    private boolean canViewReports;
    
    // Constructor
    public AdminUser() {
        super();
        this.role = Constants.ROLE_ADMIN;
        this.adminLevel = "ADMIN";
    }
    
    public AdminUser(String username, String email, String phone, String firstName, String lastName) {
        super(username, email, phone, firstName, lastName, Constants.ROLE_ADMIN);
        this.adminLevel = "ADMIN";
        this.canManageAdmins = false;
        this.canManageStaff = true;
        this.canManageConfig = true;
        this.canViewReports = true;
    }
    
    @Override
    public String getDisplayName() {
        return getFullName() + " (" + adminLevel + ")";
    }
    
    @Override
    public String getDefaultDashboardPage() {
        return "/admin/dashboard";
    }
    
    @Override
    public boolean hasPermission(String permissionCode) {
        // Admins have all permissions
        return true;
    }
    
    // Getters and Setters
    public String getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    public boolean canManageAdmins() {
        return canManageAdmins;
    }
    
    public void setCanManageAdmins(boolean canManageAdmins) {
        this.canManageAdmins = canManageAdmins;
    }
    
    public boolean canManageStaff() {
        return canManageStaff;
    }
    
    public void setCanManageStaff(boolean canManageStaff) {
        this.canManageStaff = canManageStaff;
    }
    
    public boolean canManageConfig() {
        return canManageConfig;
    }
    
    public void setCanManageConfig(boolean canManageConfig) {
        this.canManageConfig = canManageConfig;
    }
    
    public boolean canViewReports() {
        return canViewReports;
    }
    
    public void setCanViewReports(boolean canViewReports) {
        this.canViewReports = canViewReports;
    }
    
    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(adminLevel) && canManageAdmins;
    }
    
    @Override
    public String toString() {
        return super.toString() + " AdminUser{" +
                "adminLevel='" + adminLevel + '\'' +
                ", canManageAdmins=" + canManageAdmins +
                ", canManageStaff=" + canManageStaff +
                ", canManageConfig=" + canManageConfig +
                '}';
    }
}
