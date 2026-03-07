package com.example.admindashboard.controller;

import com.whitecircle.hrms.model.ServiceRequest;
import com.whitecircle.hrms.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestApiController {

    @Autowired
    private ServiceRequestRepository repository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody ServiceRequest request) {
        request.setSubmissionDate(LocalDate.now());
        ServiceRequest savedRequest = repository.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/active/{empId}")
    public List<ServiceRequest> getEmployeeRequests(@PathVariable String empId) {
        return repository.findByEmployeeIdOrderBySubmissionDateDesc(empId);
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam Long id, @RequestParam String status) {
        ServiceRequest request = repository.findById(id).orElseThrow();
        request.setStatus(status);
        repository.save(request);
        return ResponseEntity.ok("Status updated to " + status);
    }

    // Fixed the path and the repository variable name
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getRequestById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}