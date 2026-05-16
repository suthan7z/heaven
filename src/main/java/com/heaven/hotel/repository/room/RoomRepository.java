package com.heaven.hotel.repository.room;

import com.heaven.hotel.model.room.Room;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for room persistence operations
 */
public interface RoomRepository {
    
    /**
     * Save a new room
     */
    boolean save(Room room);
    
    /**
     * Update an existing room
     */
    boolean update(Room room);
    
    /**
     * Delete a room by ID
     */
    boolean delete(String roomId);
    
    /**
     * Find room by ID
     */
    Optional<Room> findById(String roomId);
    
    /**
     * Find room by room number
     */
    Optional<Room> findByNumber(int roomNumber);
    
    /**
     * Get all rooms
     */
    List<Room> findAll();
    
    /**
     * Get rooms by type
     */
    List<Room> findByType(String roomType);
    
    /**
     * Get rooms by status
     */
    List<Room> findByStatus(String status);
    
    /**
     * Get available rooms
     */
    List<Room> findAvailableRooms();
    
    /**
     * Get occupied rooms
     */
    List<Room> findOccupiedRooms();
    
    /**
     * Get rooms under maintenance
     */
    List<Room> findMaintenanceRooms();
    
    /**
     * Count total rooms
     */
    int countRooms();
    
    /**
     * Count rooms by status
     */
    int countByStatus(String status);
}
