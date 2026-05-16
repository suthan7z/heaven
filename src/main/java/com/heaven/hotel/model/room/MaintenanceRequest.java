package com.heaven.hotel.model.room;

import java.time.LocalDateTime;

/**
 * Maintenance request for rooms
 */
public class MaintenanceRequest {
    
    private String requestId;
    private String roomId;
    private String type; // CLEANING, REPAIR, INSPECTION, RENOVATION
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String description;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedAt;
    private String assignedTo; // Staff member ID
    private String notes;
    private double estimatedCost;
    private double actualCost;
    
    public MaintenanceRequest() {}
    
    public MaintenanceRequest(String requestId, String roomId, String type, String priority, String description) {
        this.requestId = requestId;
        this.roomId = roomId;
        this.type = type;
        this.priority = priority;
        this.description = description;
        this.status = "PENDING";
        this.requestedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }
    
    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
    
    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }
    
    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public double getEstimatedCost() {
        return estimatedCost;
    }
    
    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
    
    public double getActualCost() {
        return actualCost;
    }
    
    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
    }
    
    // Status helpers
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }
    
    public void markInProgress() {
        this.status = "IN_PROGRESS";
    }
    
    public void markCompleted() {
        this.status = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }
    
    public void markCancelled() {
        this.status = "CANCELLED";
    }
    
    @Override
    public String toString() {
        return "MaintenanceRequest{" +
                "requestId='" + requestId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", type='" + type + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
