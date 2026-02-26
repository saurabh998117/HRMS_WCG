package com.example.admindashboard.dto;

import java.time.LocalDate;
import java.util.List;

public class TimesheetSubmissionDTO {

    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String status; // "DRAFT" or "SUBMITTED"
    private String overallComments;
    private Double totalWeekHours;

    // The list of rows from the HTML table
    private List<TimesheetRowDTO> rows;

    // --- Getters and Setters ---
    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }

    public LocalDate getWeekEndDate() { return weekEndDate; }
    public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOverallComments() { return overallComments; }
    public void setOverallComments(String overallComments) { this.overallComments = overallComments; }

    public Double getTotalWeekHours() { return totalWeekHours; }
    public void setTotalWeekHours(Double totalWeekHours) { this.totalWeekHours = totalWeekHours; }

    public List<TimesheetRowDTO> getRows() { return rows; }
    public void setRows(List<TimesheetRowDTO> rows) { this.rows = rows; }
}