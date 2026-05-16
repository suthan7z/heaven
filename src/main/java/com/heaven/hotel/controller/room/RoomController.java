package com.heaven.hotel.controller.room;

import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.service.room.RoomService;
import com.heaven.hotel.service.room.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * Controller for room management operations
 */
@Controller
@RequestMapping("/rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private MaintenanceService maintenanceService;
    
    /**
     * Display all rooms
     */
    @GetMapping
    public String listRooms(
            @RequestParam(required = false) String filter,
            Model model) {
        
        List<Room> rooms;
        if (filter != null && !filter.isEmpty()) {
            rooms = roomService.searchRooms(filter);
            model.addAttribute("filter", filter);
        } else {
            rooms = roomService.getAllRooms();
        }
        
        model.addAttribute("rooms", rooms);
        model.addAttribute("totalRooms", roomService.getTotalRoomCount());
        model.addAttribute("occupiedRooms", roomService.getOccupiedRoomCount());
        model.addAttribute("availableRooms", roomService.getAvailableRoomCount());
        model.addAttribute("occupancyRate", String.format("%.1f%%", roomService.getOccupancyRate()));
        model.addAttribute("pageTitle", "Room Management");
        
        return "room/list";
    }
    
    /**
     * Display add room form
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("pageTitle", "Add Room");
        return "room/add";
    }
    
    /**
     * Handle add room form submission
     */
    @PostMapping("/add")
    public String addRoom(
            @RequestParam int roomNumber,
            @RequestParam String roomType,
            @RequestParam double pricePerNight,
            @RequestParam String description,
            @RequestParam(required = false, defaultValue = "false") boolean hasAC,
            @RequestParam(required = false, defaultValue = "false") boolean hasWifi,
            @RequestParam(required = false, defaultValue = "false") boolean hasTV,
            @RequestParam(required = false, defaultValue = "false") boolean hasKitchen,
            @RequestParam(required = false, defaultValue = "false") boolean hasBalcony,
            RedirectAttributes redirectAttributes) {
        
        try {
            Room room;
            
            if ("SUITE".equals(roomType)) {
                room = roomService.addSuiteRoom(roomNumber, pricePerNight, description);
            } else {
                room = roomService.addStandardRoom(roomNumber, pricePerNight, description);
            }
            
            // Set amenities
            room.setHasAC(hasAC);
            room.setHasWifi(hasWifi);
            room.setHasTV(hasTV);
            room.setHasKitchen(hasKitchen);
            room.setHasBalcony(hasBalcony);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Room " + roomNumber + " (" + roomType + ") added successfully!");
            
            return "redirect:/rooms";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to add room: " + e.getMessage());
            return "redirect:/rooms/add";
        }
    }
    
    /**
     * Display room details
     */
    @GetMapping("/{roomId}")
    public String viewRoom(@PathVariable String roomId, Model model) {
        try {
            Room room = roomService.getRoomById(roomId);
            model.addAttribute("room", room);
            model.addAttribute("pageTitle", "Room " + room.getRoomNumber());
            model.addAttribute("maintenanceHistory", maintenanceService.getRequestsByRoom(roomId));
            return "room/view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Room not found");
            return "redirect:/rooms";
        }
    }
    
    /**
     * Display edit room form
     */
    @GetMapping("/{roomId}/edit")
    public String showEditForm(@PathVariable String roomId, Model model) {
        try {
            Room room = roomService.getRoomById(roomId);
            model.addAttribute("room", room);
            model.addAttribute("pageTitle", "Edit Room " + room.getRoomNumber());
            return "room/edit";
        } catch (Exception e) {
            return "redirect:/rooms";
        }
    }
    
    /**
     * Handle edit room form submission
     */
    @PostMapping("/{roomId}/edit")
    public String editRoom(
            @PathVariable String roomId,
            @RequestParam int roomNumber,
            @RequestParam double pricePerNight,
            @RequestParam String description,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        try {
            roomService.updateRoom(roomId, roomNumber, pricePerNight, description, status);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Room " + roomNumber + " updated successfully!");
            return "redirect:/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update room: " + e.getMessage());
            return "redirect:/rooms/" + roomId + "/edit";
        }
    }
    
    /**
     * Change room status
     */
    @PostMapping("/{roomId}/status")
    public String changeStatus(
            @PathVariable String roomId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        try {
            roomService.changeRoomStatus(roomId, status);
            Room room = roomService.getRoomById(roomId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Room " + room.getRoomNumber() + " status changed to " + status);
            return "redirect:/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to change room status: " + e.getMessage());
            return "redirect:/rooms";
        }
    }
    
    /**
     * Delete room
     */
    @PostMapping("/{roomId}/delete")
    public String deleteRoom(@PathVariable String roomId, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.getRoomById(roomId);
            int roomNumber = room.getRoomNumber();
            roomService.deleteRoom(roomId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Room " + roomNumber + " deleted successfully!");
            return "redirect:/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to delete room: " + e.getMessage());
            return "redirect:/rooms";
        }
    }
    
    /**
     * View available rooms
     */
    @GetMapping("/available/list")
    public String listAvailable(Model model) {
        List<Room> availableRooms = roomService.getAvailableRooms();
        model.addAttribute("rooms", availableRooms);
        model.addAttribute("pageTitle", "Available Rooms");
        model.addAttribute("totalRooms", availableRooms.size());
        return "room/available";
    }
    
    /**
     * Request maintenance for room
     */
    @PostMapping("/{roomId}/maintenance")
    public String requestMaintenance(
            @PathVariable String roomId,
            @RequestParam String type,
            @RequestParam String priority,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {
        
        try {
            maintenanceService.createMaintenanceRequest(roomId, type, priority, description);
            Room room = roomService.getRoomById(roomId);
            roomService.markMaintenance(roomId, description);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Maintenance request created for room " + room.getRoomNumber());
            return "redirect:/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to create maintenance request: " + e.getMessage());
            return "redirect:/rooms";
        }
    }
}
