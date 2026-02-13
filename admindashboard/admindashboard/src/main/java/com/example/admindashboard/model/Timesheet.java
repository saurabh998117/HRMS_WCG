package com.example.admindashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // IMPORTANT IMPORT
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
    // --- THIS IS THE CRITICAL FIX ---
    // It tells Java: "When you show me the User, DO NOT try to show their password or their other timesheets."
    @JsonIgnoreProperties({"password", "timesheets", "roles", "attendance", "hibernateLazyInitializer", "handler"})
    private User user;
    // -------------------------------

    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private Double totalWeekHours;
    private String status;
    private String comments;

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("timesheet") // Prevents loop in entries too
    private List<TimesheetEntry> entries;

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }

    public LocalDate getWeekEndDate() { return weekEndDate; }
    public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }

    public Double getTotalWeekHours() { return totalWeekHours; }
    public void setTotalWeekHours(Double totalWeekHours) { this.totalWeekHours = totalWeekHours; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public List<TimesheetEntry> getEntries() { return entries; }
    public void setEntries(List<TimesheetEntry> entries) { this.entries = entries; }
}