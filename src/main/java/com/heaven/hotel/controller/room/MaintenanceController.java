package com.heaven.hotel.controller.room;

import com.heaven.hotel.service.room.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maintenance")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public String listRequests(Model model) {
        model.addAttribute("requests", maintenanceService.getAllRequests());
        model.addAttribute("pendingRequests", maintenanceService.getPendingRequests());
        model.addAttribute("pageTitle", "Maintenance Requests");
        return "room/maintenance";
    }
}
