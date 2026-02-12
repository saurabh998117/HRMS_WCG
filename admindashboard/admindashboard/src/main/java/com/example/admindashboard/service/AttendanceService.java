package com.example.admindashboard.service;

import com.example.admindashboard.model.Attendance;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.AttendanceRepository;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    // Logic: Employee Check-In
    public String checkIn(String username) {
        // FIX: Handle Optional<User>
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return "User not found!";
        User user = userOpt.get();

        LocalDate today = LocalDate.now();

        // Rule: Prevent Duplicate Entry
        Optional<Attendance> existing = attendanceRepository.findByUserAndDate(user, today);
        if (existing.isPresent()) {
            return "You have already checked in today!";
        }

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalTime.now());
        attendance.setStatus("Present");

        attendanceRepository.save(attendance);
        return "Check-In Successful at " + LocalTime.now();
    }

    // Logic: Employee Check-Out
    public String checkOut(String username) {
        // FIX: Handle Optional<User>
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return "User not found!";
        User user = userOpt.get();

        LocalDate today = LocalDate.now();

        // Rule: No Checkout without Checkin
        Optional<Attendance> existing = attendanceRepository.findByUserAndDate(user, today);
        if (existing.isEmpty()) {
            return "You have not checked in today!";
        }

        Attendance attendance = existing.get();
        if (attendance.getCheckOutTime() != null) {
            return "You have already checked out today!";
        }

        attendance.setCheckOutTime(LocalTime.now());

        // Rule: Auto Calculate Total Hours
        long minutes = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime()).toMinutes();
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;
        attendance.setTotalHours(hours + "h " + remainingMinutes + "m");

        attendanceRepository.save(attendance);
        return "Check-Out Successful. Total Hours: " + attendance.getTotalHours();
    }

    // --- ADMIN FEATURE ---
    // Count how many people checked in today
    public long getTodayPresentCount() {
        LocalDate today = LocalDate.now();
        // This requires a custom query or logic.
        // For simplicity, we can fetch all and filter, or add a count method in Repository.
        // Let's assume we add a method in Repository for performance.
        return attendanceRepository.countByDate(today);
    }
}