package com.heaven.hotel.service.room;

import com.heaven.hotel.model.room.Room;
import com.heaven.hotel.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for room availability checking
 */
@Service
public class AvailabilityService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    /**
     * Check if a room is available
     */
    public boolean isRoomAvailable(String roomId) {
        return roomRepository.findById(roomId)
            .map(Room::isAvailable)
            .orElse(false);
    }
    
    /**
     * Check if room can be booked (available or reserved)
     */
    public boolean canBookRoom(String roomId) {
        return roomRepository.findById(roomId)
            .map(room -> room.isAvailable() || room.isReserved())
            .orElse(false);
    }
    
    /**
     * Get available rooms for a given date range
     */
    public List<Room> getAvailableRoomsForDateRange(LocalDate checkIn, LocalDate checkOut) {
        // For now, return all available rooms
        // In a real system, this would check against bookings table
        return roomRepository.findAvailableRooms();
    }
    
    /**
     * Get available rooms by type
     */
    public List<Room> getAvailableRoomsByType(String roomType) {
        List<Room> rooms = roomRepository.findByType(roomType);
        return rooms.stream()
            .filter(Room::isAvailable)
            .toList();
    }
    
    /**
     * Get available standard rooms
     */
    public List<Room> getAvailableStandardRooms() {
        return getAvailableRoomsByType("STANDARD");
    }
    
    /**
     * Get available suite rooms
     */
    public List<Room> getAvailableSuiteRooms() {
        return getAvailableRoomsByType("SUITE");
    }
    
    /**
     * Get number of available rooms
     */
    public int getAvailableRoomCount() {
        return (int) roomRepository.findAll().stream()
            .filter(Room::isAvailable)
            .count();
    }
    
    /**
     * Get occupancy percentage
     */
    public double getOccupancyPercentage() {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms.isEmpty()) return 0;
        
        long occupiedCount = allRooms.stream()
            .filter(Room::isOccupied)
            .count();
        
        return (double) occupiedCount / allRooms.size() * 100;
    }
    
    /**
     * Check room availability by features
     */
    public List<Room> getRoomsByFeatures(boolean needsAC, boolean needsWiFi, int minCapacity) {
        return roomRepository.findAvailableRooms().stream()
            .filter(room -> {
                if (needsAC && !room.isHasAC()) return false;
                if (needsWiFi && !room.isHasWifi()) return false;
                if (room.getCapacity() < minCapacity) return false;
                return true;
            })
            .toList();
    }
    
    /**
     * Find cheapest available room
     */
    public Room findCheapestAvailableRoom() {
        return roomRepository.findAvailableRooms().stream()
            .min((r1, r2) -> Double.compare(r1.getPricePerNight(), r2.getPricePerNight()))
            .orElseThrow(() -> new RuntimeException("No available rooms"));
    }
    
    /**
     * Find available room by capacity
     */
    public Room findAvailableRoomByCapacity(int capacity) {
        return roomRepository.findAvailableRooms().stream()
            .filter(room -> room.getCapacity() >= capacity)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No available rooms with capacity " + capacity));
    }
}
