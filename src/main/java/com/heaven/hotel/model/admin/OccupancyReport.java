package com.heaven.hotel.model.admin;

import java.time.LocalDate;

public class OccupancyReport extends Report {
    private int totalRooms;
    private int occupiedRooms;
    private double occupancyRate;

    public OccupancyReport() { super(); }
    public OccupancyReport(String reportId, LocalDate startDate, LocalDate endDate, String generatedBy) {
        super(reportId, "OCCUPANCY", "Occupancy Report", startDate, endDate, generatedBy);
    }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int v) { this.totalRooms = v; }
    public int getOccupiedRooms() { return occupiedRooms; }
    public void setOccupiedRooms(int v) { this.occupiedRooms = v; }
    public double getOccupancyRate() { return occupancyRate; }
    public void setOccupancyRate(double v) { this.occupancyRate = v; }
}
