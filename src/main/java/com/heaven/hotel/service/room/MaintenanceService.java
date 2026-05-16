package com.heaven.hotel.service.room;

import com.heaven.hotel.model.room.MaintenanceRequest;
import com.heaven.hotel.repository.room.RoomRepository;
import com.heaven.hotel.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for maintenance request management
 */
@Service
public class MaintenanceService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    private List<MaintenanceRequest> maintenanceRequests = new ArrayList<>();
    
    /**
     * Create a maintenance request
     */
    public MaintenanceRequest createMaintenanceRequest(String roomId, String type, String priority, String description) {
        // Verify room exists
        roomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        
        MaintenanceRequest request = new MaintenanceRequest(
            IdGenerator.generateId("MAINT"),
            roomId,
            type,
            priority,
            description
        );
        
        maintenanceRequests.add(request);
        return request;
    }
    
    /**
     * Get all pending maintenance requests
     */
    public List<MaintenanceRequest> getPendingRequests() {
        return maintenanceRequests.stream()
            .filter(MaintenanceRequest::isPending)
            .toList();
    }
    
    /**
     * Get all maintenance requests for a room
     */
    public List<MaintenanceRequest> getRequestsByRoom(String roomId) {
        return maintenanceRequests.stream()
            .filter(r -> r.getRoomId().equals(roomId))
            .toList();
    }
    
    /**
     * Assign maintenance request to staff
     */
    public void assignRequest(String requestId, String staffId, LocalDateTime scheduledDate) {
        MaintenanceRequest request = findRequestById(requestId);
        request.setAssignedTo(staffId);
        request.setScheduledDate(scheduledDate);
    }
    
    /**
     * Mark request as in progress
     */
    public void startMaintenance(String requestId) {
        MaintenanceRequest request = findRequestById(requestId);
        request.markInProgress();
    }
    
    /**
     * Complete maintenance request
     */
    public void completeMaintenance(String requestId, double actualCost, String notes) {
        MaintenanceRequest request = findRequestById(requestId);
        request.markCompleted();
        request.setActualCost(actualCost);
        request.setNotes(notes);
    }
    
    /**
     * Cancel maintenance request
     */
    public void cancelRequest(String requestId) {
        MaintenanceRequest request = findRequestById(requestId);
        request.markCancelled();
    }
    
    /**
     * Find request by ID
     */
    private MaintenanceRequest findRequestById(String requestId) {
        return maintenanceRequests.stream()
            .filter(r -> r.getRequestId().equals(requestId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Maintenance request not found: " + requestId));
    }
    
    /**
     * Get all maintenance requests
     */
    public List<MaintenanceRequest> getAllRequests() {
        return new ArrayList<>(maintenanceRequests);
    }
    
    /**
     * Get high priority requests
     */
    public List<MaintenanceRequest> getHighPriorityRequests() {
        return maintenanceRequests.stream()
            .filter(r -> "HIGH".equals(r.getPriority()) || "URGENT".equals(r.getPriority()))
            .toList();
    }
}
