package com.example.admindashboard.controller;

import com.example.admindashboard.model.AttendanceRegularization;
import com.example.admindashboard.repository.AttendanceRegularizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/regularization")
public class AdminRegularizationController {

    @Autowired
    private AttendanceRegularizationRepository regularizationRepository;

    // 1. Get List of Requests (Pending/Approved/Rejected)
    @GetMapping("/list")
    public ResponseEntity<List<AttendanceRegularization>> getRequests(@RequestParam String status) {
        return ResponseEntity.ok(regularizationRepository.findByStatus(status));
    }

    // 2. Approve or Reject Request
    @PostMapping("/{id}/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable String status,
            @RequestParam(required = false) String comments) {

        Optional<AttendanceRegularization> requestOpt = regularizationRepository.findById(id);

        if (requestOpt.isPresent()) {
            AttendanceRegularization req = requestOpt.get();
            req.setStatus(status);

            // Ensure your Model has this field!
            if (comments != null) req.setHrComments(comments);

            regularizationRepository.save(req);
            return ResponseEntity.ok("Request updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}