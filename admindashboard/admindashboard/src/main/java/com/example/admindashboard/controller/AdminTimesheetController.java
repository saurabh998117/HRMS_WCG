package com.example.admindashboard.controller;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.repository.TimesheetRepository; // Direct Repo access for simplicity
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/timesheet")
public class AdminTimesheetController {

    @Autowired
    private TimesheetRepository timesheetRepository;

    // 1. GET List (We did this yesterday)
    @GetMapping("/list")
    public ResponseEntity<List<Timesheet>> getTimesheetsByStatus(@RequestParam String status) {
        return ResponseEntity.ok(timesheetRepository.findByStatus(status));
    }

    // 2. POST Update Status (THIS WAS MISSING!)
    // Usage: /api/admin/timesheet/5/Approved
    @PostMapping("/{id}/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable String status,
            @RequestParam(required = false) String comments) {

        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(id);

        if (optionalTimesheet.isPresent()) {
            Timesheet t = optionalTimesheet.get();
            t.setStatus(status);

            // If there's a comment (like for Rejection), save it
            if (comments != null && !comments.isEmpty()) {
                t.setComments(comments);
            }

            timesheetRepository.save(t);
            return ResponseEntity.ok("Status updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}