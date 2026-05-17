package com.heaven.hotel.controller;

import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/dashboard";
        }
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("totalRooms", roomService.getTotalRoomCount());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Us");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact Us");
        return "contact";
    }
}
