package com.example.admindashboard.controller;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/timesheet")
public class AdminTimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    // API: View all Pending Timesheets
    @GetMapping("/pending")
    public ResponseEntity<List<Timesheet>> getPendingTimesheets() {
        return ResponseEntity.ok(timesheetService.getPendingTimesheets());
    }

    // API: Approve a Timesheet
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveTimesheet(@PathVariable Long id) {
        String result = timesheetService.approveTimesheet(id);
        return ResponseEntity.ok(result);
    }

    // API: Reject a Timesheet (Pass reason in body or query param)
    // Example usage: PUT /api/admin/timesheet/reject/5?reason=Incomplete+Data
    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectTimesheet(@PathVariable Long id, @RequestParam String reason) {
        String result = timesheetService.rejectTimesheet(id, reason);
        return ResponseEntity.ok(result);
    }
}