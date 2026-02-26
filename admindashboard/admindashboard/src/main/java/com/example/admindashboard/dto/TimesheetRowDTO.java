package com.example.admindashboard.dto;

public class TimesheetRowDTO {

    private Long projectId;
    private Long taskId;
    private String type;

    private Double sun;
    private Double mon;
    private Double tue;
    private Double wed;
    private Double thu;
    private Double fri;
    private Double sat;

    private Double rowTotal;
    private String details;

    // --- Getters and Setters ---
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getSun() { return sun; }
    public void setSun(Double sun) { this.sun = sun; }

    public Double getMon() { return mon; }
    public void setMon(Double mon) { this.mon = mon; }

    public Double getTue() { return tue; }
    public void setTue(Double tue) { this.tue = tue; }

    public Double getWed() { return wed; }
    public void setWed(Double wed) { this.wed = wed; }

    public Double getThu() { return thu; }
    public void setThu(Double thu) { this.thu = thu; }

    public Double getFri() { return fri; }
    public void setFri(Double fri) { this.fri = fri; }

    public Double getSat() { return sat; }
    public void setSat(Double sat) { this.sat = sat; }

    public Double getRowTotal() { return rowTotal; }
    public void setRowTotal(Double rowTotal) { this.rowTotal = rowTotal; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}