package com.heaven.hotel.controller.user;

import com.heaven.hotel.service.user.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasRole('ADMIN')")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("staff", staffService.getAllStaff());
        model.addAttribute("pageTitle", "Staff Management");
        return "user/staff";
    }
}
