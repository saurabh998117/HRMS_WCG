package com.example.admindashboard.controller; // Change this if your package name is different!

import com.example.admindashboard.dto.TimesheetSubmissionDTO;
import com.example.admindashboard.model.WeeklyTimesheet;
import com.example.admindashboard.service.WeeklyTimesheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/weekly-timesheet")
public class WeeklyTimesheetRestController {

    @Autowired
    private WeeklyTimesheetService timesheetService;

    // 1. Existing POST endpoint to save/submit
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitTimesheet(@RequestBody TimesheetSubmissionDTO payload) {
        Map<String, String> response = new HashMap<>();
        try {
            timesheetService.saveWeeklyTimesheet(payload);
            response.put("status", "success");
            response.put("message", "Timesheet saved successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 2. Existing GET endpoint for the Hub page (Recent Timecards & Calendar)
    @GetMapping("/my-timesheets")
    public ResponseEntity<List<WeeklyTimesheet>> getMyTimesheets() {
        List<WeeklyTimesheet> timesheets = timesheetService.getAllMyTimesheets();
        return ResponseEntity.ok(timesheets);
    }

    // 3. NEW GET endpoint we just added!
    // This fetches a single specific week so the create-timesheet page can load your Drafts
    @GetMapping("/week")
    public ResponseEntity<WeeklyTimesheet> getTimesheetForWeek(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        Optional<WeeklyTimesheet> timesheet = timesheetService.getTimesheetByWeekStartDate(startDate);

        // If it exists in the DB, return it. If not, return a 204 No Content status.
        return timesheet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}