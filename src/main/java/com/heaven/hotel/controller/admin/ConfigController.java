package com.heaven.hotel.controller.admin;

import com.heaven.hotel.service.admin.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
@PreAuthorize("hasRole('ADMIN')")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public String config(Model model) {
        model.addAttribute("config", configService.getConfig());
        model.addAttribute("pageTitle", "Hotel Configuration");
        return "admin/config";
    }
}
