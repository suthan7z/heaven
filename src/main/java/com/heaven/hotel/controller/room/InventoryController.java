package com.heaven.hotel.controller.room;

import com.heaven.hotel.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rooms/inventory")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class InventoryController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String inventory(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("totalRooms", roomService.getTotalRoomCount());
        model.addAttribute("availableRooms", roomService.getAvailableRoomCount());
        model.addAttribute("occupiedRooms", roomService.getOccupiedRoomCount());
        model.addAttribute("pageTitle", "Room Inventory");
        return "room/inventory";
    }
}
