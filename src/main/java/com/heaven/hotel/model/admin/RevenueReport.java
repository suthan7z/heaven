package com.heaven.hotel.model.admin;

import java.time.LocalDate;

public class RevenueReport extends Report {
    private double totalRevenue;
    private double totalTax;
    private int totalBookings;

    public RevenueReport() { super(); }
    public RevenueReport(String reportId, LocalDate startDate, LocalDate endDate, String generatedBy) {
        super(reportId, "REVENUE", "Revenue Report", startDate, endDate, generatedBy);
    }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double v) { this.totalRevenue = v; }
    public double getTotalTax() { return totalTax; }
    public void setTotalTax(double v) { this.totalTax = v; }
    public int getTotalBookings() { return totalBookings; }
    public void setTotalBookings(int v) { this.totalBookings = v; }
}
