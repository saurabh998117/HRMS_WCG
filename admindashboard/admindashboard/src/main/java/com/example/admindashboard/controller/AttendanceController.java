package com.example.admindashboard.controller;

import com.example.admindashboard.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // API 1: Check-In
    // The "Principal" object automatically gets the logged-in user's username
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(Principal principal) {
        String result = attendanceService.checkIn(principal.getName());
        if (result.contains("Successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // API 2: Check-Out
    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(Principal principal) {
        String result = attendanceService.checkOut(principal.getName());
        if (result.contains("Successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAttendanceDraft(@RequestBody java.util.Map<String, Object> attendanceData, Principal principal) {
        try {
            // We pass the raw data and the logged-in username to the service layer
            Object savedDraft = attendanceService.saveWeeklyDraft(attendanceData, principal.getName());
            return ResponseEntity.ok(savedDraft);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving draft: " + e.getMessage());
        }
    }

    // API 4: Submit for Approval
    @PostMapping("/submit/{id}")
    public ResponseEntity<?> submitAttendance(@PathVariable Long id, Principal principal) {
        try {
            // Tells the service to change the status from "Draft" to "Pending"
            attendanceService.submitWeeklyAttendance(id, principal.getName());
            return ResponseEntity.ok("Successfully submitted for approval.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting record: " + e.getMessage());
        }
    }

    // API 5: Get My Attendance History
    @GetMapping("/my-history")
    public ResponseEntity<?> getMyHistory(Principal principal) {
        try {
            return ResponseEntity.ok(attendanceService.getMyAttendanceHistory(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching history: " + e.getMessage());
        }
    }


}