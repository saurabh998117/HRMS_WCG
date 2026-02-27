package com.example.admindashboard.controller;

import com.example.admindashboard.dto.TimesheetSubmissionDTO;
import com.example.admindashboard.model.WeeklyTimesheet;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.UserRepository;
import com.example.admindashboard.service.WeeklyTimesheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // UPDATED: Using your actual UserRepository
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Safety check to ensure someone is actually logged in
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized shortcut
        }

        String currentUsername = authentication.getName(); // This gets "EMP001"

        // UPDATED: Look them up in the User database
        Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);

        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found shortcut
        }

        User currentUser = currentUserOpt.get();

        Map<String, Object> response = new HashMap<>();

        // Send "EMP001" to be displayed on the screen
        response.put("employeeId", currentUser.getUsername());

        // Send their full name (Based on your repository, it looks like you use getFullName())
        response.put("employeeName", currentUser.getFullName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-timesheets")
    public ResponseEntity<List<WeeklyTimesheet>> getMyTimesheets() {
        List<WeeklyTimesheet> timesheets = timesheetService.getAllMyTimesheets();
        return ResponseEntity.ok(timesheets);
    }

    @GetMapping("/week")
    public ResponseEntity<WeeklyTimesheet> getTimesheetForWeek(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        Optional<WeeklyTimesheet> timesheet = timesheetService.getTimesheetByWeekStartDate(startDate);

        return timesheet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}