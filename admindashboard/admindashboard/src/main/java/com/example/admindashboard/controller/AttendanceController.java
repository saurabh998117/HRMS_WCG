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
}