package com.heaven.hotel.model.admin;

public class HotelConfig {
    private String configId;
    private String hotelName;
    private String address;
    private String phone;
    private String email;
    private double taxRate;
    private int checkInHour;
    private int checkOutHour;
    private int maxRoomCapacity;

    public HotelConfig() {
        this.taxRate = 0.10;
        this.checkInHour = 14;
        this.checkOutHour = 12;
        this.maxRoomCapacity = 4;
    }

    public String getConfigId() { return configId; }
    public void setConfigId(String v) { this.configId = v; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String v) { this.hotelName = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }
    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double v) { this.taxRate = v; }
    public int getCheckInHour() { return checkInHour; }
    public void setCheckInHour(int v) { this.checkInHour = v; }
    public int getCheckOutHour() { return checkOutHour; }
    public void setCheckOutHour(int v) { this.checkOutHour = v; }
    public int getMaxRoomCapacity() { return maxRoomCapacity; }
    public void setMaxRoomCapacity(int v) { this.maxRoomCapacity = v; }
}
