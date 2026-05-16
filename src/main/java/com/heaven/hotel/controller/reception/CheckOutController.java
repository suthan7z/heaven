package com.heaven.hotel.controller.reception;

import com.heaven.hotel.service.reception.CheckOutService;
import com.heaven.hotel.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reception/checkout")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String checkOutPage(Model model) {
        model.addAttribute("checkedInBookings", bookingService.getConfirmedBookings());
        model.addAttribute("pageTitle", "Check-Out");
        return "reception/checkout";
    }

    @PostMapping("/{bookingId}")
    public String processCheckOut(@PathVariable String bookingId, Authentication auth,
                                  RedirectAttributes ra) {
        try {
            checkOutService.processCheckOut(bookingId, auth.getName());
            ra.addFlashAttribute("successMessage", "Guest checked out successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reception/checkout";
    }
}
