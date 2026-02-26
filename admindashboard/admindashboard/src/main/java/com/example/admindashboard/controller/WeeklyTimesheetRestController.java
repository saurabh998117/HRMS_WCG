package com.example.admindashboard.controller;

import com.example.admindashboard.dto.TimesheetSubmissionDTO;
import com.example.admindashboard.service.WeeklyTimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weekly-timesheet")
public class WeeklyTimesheetRestController {

    // UNCOMMENTED: Inject the new service!
    @Autowired
    private WeeklyTimesheetService timesheetService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitWeeklyTimesheet(@RequestBody TimesheetSubmissionDTO payload) {
        try {
            System.out.println("Processing Timesheet for Week: " + payload.getWeekStartDate());

            // THE MAGIC HAPPENS HERE: Call the service to save to MySQL
            timesheetService.saveWeeklyTimesheet(payload);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Timesheet successfully saved as " + payload.getStatus());
            response.put("status", "success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to save timesheet: " + e.getMessage());
            errorResponse.put("status", "error");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}