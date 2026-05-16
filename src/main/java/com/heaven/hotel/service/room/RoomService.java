package com.heaven.hotel.service.room;

import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.model.room.StandardRoom;
import com.heaven.hotel.model.room.SuiteRoom;
import com.heaven.hotel.repository.room.RoomRepository;
import com.heaven.hotel.utils.Constants;
import com.heaven.hotel.utils.IdGenerator;
import com.heaven.hotel.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for room management operations
 */
@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    /**
     * Add a new standard room
     */
    public Room addStandardRoom(int roomNumber, double pricePerNight, String description) {
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        // Check if room number already exists
        if (roomRepository.findByNumber(roomNumber).isPresent()) {
            throw new IllegalArgumentException("Room number " + roomNumber + " already exists");
        }
        
        StandardRoom room = new StandardRoom(
            IdGenerator.generateId("ROOM"),
            roomNumber,
            pricePerNight,
            description
        );
        
        if (roomRepository.save(room)) {
            return room;
        }
        
        throw new RuntimeException("Failed to save room");
    }
    
    /**
     * Add a new suite room
     */
    public Room addSuiteRoom(int roomNumber, double basePrice, String description) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        // Check if room number already exists
        if (roomRepository.findByNumber(roomNumber).isPresent()) {
            throw new IllegalArgumentException("Room number " + roomNumber + " already exists");
        }
        
        SuiteRoom room = new SuiteRoom(
            IdGenerator.generateId("ROOM"),
            roomNumber,
            basePrice,
            description
        );
        
        if (roomRepository.save(room)) {
            return room;
        }
        
        throw new RuntimeException("Failed to save room");
    }
    
    /**
     * Get room by ID
     */
    public Room getRoomById(String roomId) {
        if (ValidationUtil.isEmpty(roomId)) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        
        return roomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
    }
    
    /**
     * Get room by room number
     */
    public Room getRoomByNumber(int roomNumber) {
        return roomRepository.findByNumber(roomNumber)
            .orElseThrow(() -> new RuntimeException("Room number " + roomNumber + " not found"));
    }
    
    /**
     * Update room
     */
    public void updateRoom(String roomId, int roomNumber, double price, String description, String status) {
        Room room = getRoomById(roomId);
        
        room.setRoomNumber(roomNumber);
        room.setPricePerNight(price);
        room.setDescription(description);
        room.setStatus(status);
        
        if (!roomRepository.update(room)) {
            throw new RuntimeException("Failed to update room");
        }
    }
    
    /**
     * Delete room
     */
    public void deleteRoom(String roomId) {
        getRoomById(roomId); // Verify room exists
        
        if (!roomRepository.delete(roomId)) {
            throw new RuntimeException("Failed to delete room");
        }
    }
    
    /**
     * Get all rooms
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    /**
     * Get rooms by type (STANDARD or SUITE)
     */
    public List<Room> getRoomsByType(String roomType) {
        if (ValidationUtil.isEmpty(roomType)) {
            throw new IllegalArgumentException("Room type cannot be empty");
        }
        
        return roomRepository.findByType(roomType);
    }
    
    /**
     * Get available rooms
     */
    public List<Room> getAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }
    
    /**
     * Get occupied rooms
     */
    public List<Room> getOccupiedRooms() {
        return roomRepository.findOccupiedRooms();
    }
    
    /**
     * Get rooms under maintenance
     */
    public List<Room> getMaintenanceRooms() {
        return roomRepository.findMaintenanceRooms();
    }
    
    /**
     * Change room status
     */
    public void changeRoomStatus(String roomId, String newStatus) {
        Room room = getRoomById(roomId);
        
        // Validate status
        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid room status: " + newStatus);
        }
        
        room.setStatus(newStatus);
        
        if (!roomRepository.update(room)) {
            throw new RuntimeException("Failed to update room status");
        }
    }
    
    /**
     * Mark room as under maintenance
     */
    public void markMaintenance(String roomId, String notes) {
        Room room = getRoomById(roomId);
        room.markMaintenance();
        room.setMaintenanceNotes(notes);
        
        if (!roomRepository.update(room)) {
            throw new RuntimeException("Failed to mark room as maintenance");
        }
    }
    
    /**
     * Search rooms by criteria
     */
    public List<Room> searchRooms(String criteria) {
        if (ValidationUtil.isEmpty(criteria)) {
            return getAllRooms();
        }
        
        List<Room> allRooms = getAllRooms();
        return allRooms.stream()
            .filter(room -> room.getRoomType().toLowerCase().contains(criteria.toLowerCase()) ||
                          room.getDescription().toLowerCase().contains(criteria.toLowerCase()) ||
                          String.valueOf(room.getRoomNumber()).contains(criteria))
            .toList();
    }
    
    /**
     * Get room count statistics
     */
    public int getTotalRoomCount() {
        return roomRepository.countRooms();
    }
    
    /**
     * Get occupied room count
     */
    public int getOccupiedRoomCount() {
        return roomRepository.countByStatus(Constants.ROOM_STATUS_OCCUPIED);
    }
    
    /**
     * Get available room count
     */
    public int getAvailableRoomCount() {
        return roomRepository.countByStatus(Constants.ROOM_STATUS_AVAILABLE);
    }
    
    /**
     * Get occupancy rate
     */
    public double getOccupancyRate() {
        int total = getTotalRoomCount();
        if (total == 0) return 0;
        return (double) getOccupiedRoomCount() / total * 100;
    }
    
    /**
     * Validate room status
     */
    private boolean isValidStatus(String status) {
        return Constants.ROOM_STATUS_AVAILABLE.equals(status) ||
               Constants.ROOM_STATUS_OCCUPIED.equals(status) ||
               Constants.ROOM_STATUS_MAINTENANCE.equals(status) ||
               Constants.ROOM_STATUS_RESERVED.equals(status);
    }
}
