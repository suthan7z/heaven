package com.heaven.hotel.model.admin;

import java.time.LocalDate;

/**
 * Occupancy report — shows room utilisation for a given period.
 * OOP: Inheritance (extends Report), Polymorphism (gatherData/formatOutput overrides)
 */
public class OccupancyReport extends Report {

    private int    totalRooms;
    private int    occupiedRooms;
    private double occupancyRate;  // percentage

    public OccupancyReport() { super(); }

    public OccupancyReport(String reportId, LocalDate startDate,
                           LocalDate endDate, String generatedBy) {
        super(reportId, generatedBy, startDate + " to " + endDate);
        this.startDate  = startDate;
        this.endDate    = endDate;
        setReportType("OCCUPANCY");
        setTitle("Occupancy Report");
    }

    // ── Abstract method implementations ──────────────────────────────────────

    /**
     * Reads bookings.txt and counts occupied rooms in the date range.
     * (Actual file parsing delegated to service layer; values set via setters.)
     */
    @Override
    public void gatherData() {
        // Occupancy rate is calculated from data injected by the service layer
        if (totalRooms > 0)
            occupancyRate = (double) occupiedRooms / totalRooms * 100;
    }

    /** Returns a formatted occupancy summary string. */
    @Override
    public String formatOutput() {
        return "--- Occupancy Report ---\n"
                + "Period       : " + getDateRange() + "\n"
                + "Total Rooms  : " + totalRooms + "\n"
                + "Occupied     : " + occupiedRooms + "\n"
                + "Available    : " + (totalRooms - occupiedRooms) + "\n"
                + "Occupancy    : " + String.format("%.1f%%", occupancyRate) + "\n";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int    getTotalRooms()           { return totalRooms; }
    public void   setTotalRooms(int v)      { this.totalRooms = v; }

    public int    getOccupiedRooms()        { return occupiedRooms; }
    public void   setOccupiedRooms(int v)   { this.occupiedRooms = v; }

    public double getOccupancyRate()        { return occupancyRate; }
    public void   setOccupancyRate(double v){ this.occupancyRate = v; }
}
