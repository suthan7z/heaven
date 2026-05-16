package com.heaven.hotel.controller.admin;

import com.heaven.hotel.service.admin.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "admin/dashboard";
    }
}
