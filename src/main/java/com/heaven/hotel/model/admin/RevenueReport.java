package com.heaven.hotel.model.admin;

import java.time.LocalDate;

/**
 * Revenue report — summarises income collected during a period.
 * OOP: Inheritance (extends Report), Polymorphism (gatherData/formatOutput overrides)
 */
public class RevenueReport extends Report {

    private double totalRevenue;
    private double totalTax;
    private int    totalBookings;
    private double averageStayValue;

    public RevenueReport() { super(); }

    public RevenueReport(String reportId, LocalDate startDate,
                         LocalDate endDate, String generatedBy) {
        super(reportId, generatedBy, startDate + " to " + endDate);
        this.startDate = startDate;
        this.endDate   = endDate;
        setReportType("REVENUE");
        setTitle("Revenue Report");
    }

    // ── Abstract method implementations ──────────────────────────────────────

    /**
     * Reads payments.txt and sums completed transaction amounts.
     * (Values injected by service layer via setters before calling generate().)
     */
    @Override
    public void gatherData() {
        if (totalBookings > 0)
            averageStayValue = totalRevenue / totalBookings;
    }

    /** Returns a formatted revenue summary string. */
    @Override
    public String formatOutput() {
        return "--- Revenue Report ---\n"
                + "Period           : " + getDateRange() + "\n"
                + "Total Bookings   : " + totalBookings + "\n"
                + "Total Revenue    : LKR " + String.format("%,.2f", totalRevenue) + "\n"
                + "Total Tax        : LKR " + String.format("%,.2f", totalTax) + "\n"
                + "Avg Stay Value   : LKR " + String.format("%,.2f", averageStayValue) + "\n"
                + "Net Revenue      : LKR " + String.format("%,.2f", totalRevenue - totalTax) + "\n";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public double getTotalRevenue()          { return totalRevenue; }
    public void   setTotalRevenue(double v)  { this.totalRevenue = v; }

    public double getTotalTax()              { return totalTax; }
    public void   setTotalTax(double v)      { this.totalTax = v; }

    public int    getTotalBookings()         { return totalBookings; }
    public void   setTotalBookings(int v)    { this.totalBookings = v; }

    public double getAverageStayValue()      { return averageStayValue; }
    public void   setAverageStayValue(double v){ this.averageStayValue = v; }
}
