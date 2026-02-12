package com.example.admindashboard.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_regularization")
public class AttendanceRegularization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date;
    private LocalTime correctCheckIn;
    private LocalTime correctCheckOut;
    private String reason;
    private String status; // Pending, Approved, Rejected

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getCorrectCheckIn() { return correctCheckIn; }
    public void setCorrectCheckIn(LocalTime correctCheckIn) { this.correctCheckIn = correctCheckIn; }
    public LocalTime getCorrectCheckOut() { return correctCheckOut; }
    public void setCorrectCheckOut(LocalTime correctCheckOut) { this.correctCheckOut = correctCheckOut; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}