package com.heaven.hotel.model.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Report {
    private String reportId;
    private String reportType;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private String summary;

    public Report() {}
    public Report(String reportId, String reportType, String title, LocalDate startDate, LocalDate endDate, String generatedBy) {
        this.reportId = reportId; this.reportType = reportType; this.title = title;
        this.startDate = startDate; this.endDate = endDate; this.generatedBy = generatedBy;
        this.generatedAt = LocalDateTime.now();
    }

    public String getReportId() { return reportId; }
    public void setReportId(String v) { this.reportId = v; }
    public String getReportType() { return reportType; }
    public void setReportType(String v) { this.reportType = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate v) { this.startDate = v; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate v) { this.endDate = v; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime v) { this.generatedAt = v; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String v) { this.generatedBy = v; }
    public String getSummary() { return summary; }
    public void setSummary(String v) { this.summary = v; }
}
