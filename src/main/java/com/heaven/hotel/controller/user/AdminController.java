package com.heaven.hotel.controller.user;

import com.heaven.hotel.service.user.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String listAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("pageTitle", "Admin Users");
        return "user/admins";
    }
}
