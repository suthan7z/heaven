package com.heaven.hotel.model.admin;

import com.heaven.hotel.model.user.AdminUser;

public class AdminManager extends AdminUser {
    private int managedAdminsCount;

    public AdminManager() { super(); }
    public AdminManager(String username, String email, String phone, String firstName, String lastName) {
        super(username, email, phone, firstName, lastName);
        setCanManageAdmins(true);
    }

    public int getManagedAdminsCount() { return managedAdminsCount; }
    public void setManagedAdminsCount(int v) { this.managedAdminsCount = v; }
}
