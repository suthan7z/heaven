package com.heaven.hotel.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    @GetMapping
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Reports");
        return "admin/reports";
    }
}
