package com.heaven.hotel.service.admin;

import com.heaven.hotel.service.booking.BookingService;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRooms", roomService.getTotalRoomCount());
        stats.put("availableRooms", roomService.getAvailableRoomCount());
        stats.put("occupiedRooms", roomService.getOccupiedRoomCount());
        stats.put("occupancyRate", String.format("%.1f%%", roomService.getOccupancyRate()));
        stats.put("totalBookings", bookingService.getAllBookings().size());
        stats.put("pendingBookings", bookingService.getPendingBookings().size());
        stats.put("confirmedBookings", bookingService.getConfirmedBookings().size());
        stats.put("totalUsers", userService.getTotalUserCount());
        stats.put("totalGuests", userService.getUserCountByRole("GUEST"));
        stats.put("totalStaff", userService.getUserCountByRole("STAFF"));
        return stats;
    }
}
