package com.heaven.hotel.controller.reception;

import com.heaven.hotel.service.reception.CheckInService;
import com.heaven.hotel.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reception/checkin")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String checkInPage(Model model) {
        model.addAttribute("confirmedBookings", bookingService.getConfirmedBookings());
        model.addAttribute("pageTitle", "Check-In");
        return "reception/checkin";
    }

    @PostMapping("/{bookingId}")
    public String processCheckIn(@PathVariable String bookingId, Authentication auth,
                                 RedirectAttributes ra) {
        try {
            checkInService.processCheckIn(bookingId, auth.getName());
            ra.addFlashAttribute("successMessage", "Guest checked in successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reception/checkin";
    }
}
