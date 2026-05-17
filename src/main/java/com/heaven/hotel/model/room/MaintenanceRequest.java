package com.heaven.hotel.model.room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Maintenance request raised against a hotel room.
 */
public class MaintenanceRequest {

    private String requestId;
    private String roomId;
    private String issueType;      // "Electrical","Plumbing","HVAC","Cleaning","Repair"
    private String priority;       // "Low","Medium","High","Urgent"
    private String assignedStaff;  // employee ID of assigned staff member
    private String status;         // "Pending","In Progress","Resolved","Cancelled"
    private String reportedDate;   // YYYY-MM-DD

    // Extra detail fields
    private String description;
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedAt;
    private String notes;
    private double estimatedCost;
    private double actualCost;

    public MaintenanceRequest() {}

    public MaintenanceRequest(String requestId, String roomId, String issueType,
                              String priority, String assignedStaff,
                              String status, String reportedDate) {
        this.requestId    = requestId;
        this.roomId       = roomId;
        this.issueType    = issueType;
        this.priority     = priority;
        this.assignedStaff = assignedStaff;
        this.status       = status;
        this.reportedDate = reportedDate;
        this.requestedAt  = LocalDateTime.now();
    }

    /** Convenience constructor used by MaintenanceService */
    public MaintenanceRequest(String requestId, String roomId,
                              String type, String priority, String description) {
        this(requestId, roomId, type, priority, "",
             "Pending", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.description = description;
    }

    // ── Status transitions ────────────────────────────────────────────────────

    public boolean isPending()     { return "Pending".equalsIgnoreCase(status); }

    public void markResolved() {
        this.status      = "Resolved";
        this.completedAt = LocalDateTime.now();
    }

    public void markInProgress()   { this.status = "In Progress"; }
    public void markCompleted()    { markResolved(); }
    public void markCancelled()    { this.status = "Cancelled"; }

    /** Escalates priority to High (or Urgent if already High). */
    public void escalate() {
        if ("Low".equalsIgnoreCase(priority))    { priority = "Medium"; return; }
        if ("Medium".equalsIgnoreCase(priority)) { priority = "High";   return; }
        priority = "Urgent";
    }

    // ── File serialisation ────────────────────────────────────────────────────

    /**
     * Format: requestId|roomId|issueType|priority|assignedStaff|status|reportedDate
     */
    @Override
    public String toString() {
        return requestId + "|" + roomId + "|" + nvl(issueType) + "|" + nvl(priority)
                + "|" + nvl(assignedStaff) + "|" + nvl(status) + "|" + nvl(reportedDate);
    }

    public static MaintenanceRequest fromString(String line) {
        String[] f = line.split("\\|", -1);
        MaintenanceRequest r = new MaintenanceRequest();
        if (f.length > 0) r.setRequestId(f[0]);
        if (f.length > 1) r.setRoomId(f[1]);
        if (f.length > 2) r.setIssueType(f[2]);
        if (f.length > 3) r.setPriority(f[3]);
        if (f.length > 4) r.setAssignedStaff(f[4]);
        if (f.length > 5) r.setStatus(f[5]);
        if (f.length > 6) r.setReportedDate(f[6]);
        return r;
    }

    private static String nvl(String s) { return s != null ? s : ""; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getRequestId()               { return requestId; }
    public void   setRequestId(String id)      { this.requestId = id; }

    public String getRoomId()                  { return roomId; }
    public void   setRoomId(String id)         { this.roomId = id; }

    public String getIssueType()               { return issueType; }
    public void   setIssueType(String t)       { this.issueType = t; }
    public String getType()                    { return issueType; }  // alias
    public void   setType(String t)            { this.issueType = t; }

    public String getPriority()                { return priority; }
    public void   setPriority(String p)        { this.priority = p; }

    public String getAssignedStaff()           { return assignedStaff; }
    public void   setAssignedStaff(String id)  { this.assignedStaff = id; }
    public String getAssignedTo()              { return assignedStaff; }
    public void   setAssignedTo(String id)     { this.assignedStaff = id; }

    public String getStatus()                  { return status; }
    public void   setStatus(String s)          { this.status = s; }

    public String getReportedDate()            { return reportedDate; }
    public void   setReportedDate(String d)    { this.reportedDate = d; }

    public String getDescription()             { return description; }
    public void   setDescription(String d)     { this.description = d; }

    public LocalDateTime getRequestedAt()      { return requestedAt; }
    public void setRequestedAt(LocalDateTime v){ this.requestedAt = v; }

    public LocalDateTime getScheduledDate()    { return scheduledDate; }
    public void setScheduledDate(LocalDateTime v){ this.scheduledDate = v; }

    public LocalDateTime getCompletedAt()      { return completedAt; }
    public void setCompletedAt(LocalDateTime v){ this.completedAt = v; }

    public String getNotes()                   { return notes; }
    public void   setNotes(String n)           { this.notes = n; }

    public double getEstimatedCost()           { return estimatedCost; }
    public void   setEstimatedCost(double c)   { this.estimatedCost = c; }

    public double getActualCost()              { return actualCost; }
    public void   setActualCost(double c)      { this.actualCost = c; }
}
