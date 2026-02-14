package com.example.admindashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "timesheets")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "timesheets", "roles", "attendance", "hibernateLazyInitializer", "handler"})
    private User user;

    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String status;
    private String comments;

    // --- ONLY KEEP THESE ONCE ---
    @Column(name = "total_hours")
    private Double totalHours;

    @Column(name = "monday_hours")
    private Double mondayHours;

    @Column(name = "monday_task")
    private String mondayTask;

    @Column(name = "tuesday_hours")
    private Double tuesdayHours;

    @Column(name = "tuesday_task")
    private String tuesdayTask;

    @Column(name = "wednesday_hours")
    private Double wednesdayHours;

    @Column(name = "wednesday_task")
    private String wednesdayTask;

    @Column(name = "thursday_hours")
    private Double thursdayHours;

    @Column(name = "thursday_task")
    private String thursdayTask;

    @Column(name = "friday_hours")
    private Double fridayHours;

    @Column(name = "friday_task")
    private String fridayTask;

    // Keep your entries list if you plan to use it later
    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("timesheet")
    private List<TimesheetEntry> entries;

    // --- UPDATED GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }

    public LocalDate getWeekEndDate() { return weekEndDate; }
    public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    // Matches the field 'totalHours'
    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }

    public Double getMondayHours() { return mondayHours; }
    public void setMondayHours(Double mondayHours) { this.mondayHours = mondayHours; }

    public String getMondayTask() { return mondayTask; }
    public void setMondayTask(String mondayTask) { this.mondayTask = mondayTask; }

    public Double getTuesdayHours() { return tuesdayHours; }
    public void setTuesdayHours(Double tuesdayHours) { this.tuesdayHours = tuesdayHours; }

    public String getTuesdayTask() { return tuesdayTask; }
    public void setTuesdayTask(String tuesdayTask) { this.tuesdayTask = tuesdayTask; }

    public Double getWednesdayHours() { return wednesdayHours; }
    public void setWednesdayHours(Double wednesdayHours) { this.wednesdayHours = wednesdayHours; }

    public String getWednesdayTask() { return wednesdayTask; }
    public void setWednesdayTask(String wednesdayTask) { this.wednesdayTask = wednesdayTask; }

    public Double getThursdayHours() { return thursdayHours; }
    public void setThursdayHours(Double thursdayHours) { this.thursdayHours = thursdayHours; }

    public String getThursdayTask() { return thursdayTask; }
    public void setThursdayTask(String thursdayTask) { this.thursdayTask = thursdayTask; }

    public Double getFridayHours() { return fridayHours; }
    public void setFridayHours(Double fridayHours) { this.fridayHours = fridayHours; }

    public String getFridayTask() { return fridayTask; }
    public void setFridayTask(String fridayTask) { this.fridayTask = fridayTask; }

    public List<TimesheetEntry> getEntries() { return entries; }
    public void setEntries(List<TimesheetEntry> entries) { this.entries = entries; }
}