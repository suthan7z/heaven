package com.heaven.hotel.controller.user;

import com.heaven.hotel.service.admin.DashboardService;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("recentBookings", bookingService.getConfirmedBookings());
        model.addAttribute("availableRooms", roomService.getAvailableRooms());
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("username", auth != null ? auth.getName() : "");
        model.addAttribute("pageTitle", "My Profile");
        return "profile";
    }
}
