package com.heaven.hotel.service.admin;

import com.heaven.hotel.model.admin.OccupancyReport;
import com.heaven.hotel.model.admin.RevenueReport;
import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ReportService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    public OccupancyReport generateOccupancyReport(LocalDate start, LocalDate end, String generatedBy) {
        OccupancyReport report = new OccupancyReport(IdGenerator.generateUUID(), start, end, generatedBy);
        report.setTotalRooms(roomService.getTotalRoomCount());
        report.setOccupiedRooms(roomService.getOccupiedRoomCount());
        report.setOccupancyRate(roomService.getOccupancyRate());
        return report;
    }

    public RevenueReport generateRevenueReport(LocalDate start, LocalDate end, String generatedBy) {
        RevenueReport report = new RevenueReport(IdGenerator.generateUUID(), start, end, generatedBy);
        double revenue = bookingService.getRevenueForDateRange(start, end);
        report.setTotalRevenue(revenue);
        report.setTotalTax(revenue * 0.10);
        report.setTotalBookings(bookingService.getBookingsForDateRange(start, end).size());
        return report;
    }
}
