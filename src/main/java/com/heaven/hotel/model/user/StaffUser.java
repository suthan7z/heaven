package com.heaven.hotel.model.user;

import com.heaven.hotel.utils.Constants;

/**
 * Staff user - reception and room management
 */
public class StaffUser extends User {
    
    private String department;
    private String position;
    private String shift;
    private String managerId;
    private double salary;
    private String employmentStatus;
    
    // Constructor
    public StaffUser() {
        super();
        this.role = Constants.ROLE_STAFF;
        this.employmentStatus = "ACTIVE";
    }
    
    public StaffUser(String username, String email, String phone, String firstName, String lastName, 
                     String department, String position) {
        super(username, email, phone, firstName, lastName, Constants.ROLE_STAFF);
        this.department = department;
        this.position = position;
        this.employmentStatus = "ACTIVE";
    }
    
    @Override
    public String getDisplayName() {
        return getFullName() + " (" + position + ")";
    }
    
    @Override
    public String getDefaultDashboardPage() {
        return "/dashboard";
    }
    
    @Override
    public boolean hasPermission(String permissionCode) {
        switch (permissionCode) {
            case "view_rooms":
            case "manage_rooms":
            case "check_in":
            case "check_out":
            case "issue_keycard":
            case "view_booking":
            case "manage_booking":
            case "add_charges":
            case "view_profile":
            case "edit_profile":
            case "view_guests":
            case "manage_maintenance":
                return true;
            default:
                return false;
        }
    }
    
    // Getters and Setters
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        this.shift = shift;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public String getEmploymentStatus() {
        return employmentStatus;
    }
    
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    
    public boolean isEmployed() {
        return "ACTIVE".equals(employmentStatus);
    }
    
    @Override
    public String toString() {
        return super.toString() + " StaffUser{" +
                "department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", shift='" + shift + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                '}';
    }
}
