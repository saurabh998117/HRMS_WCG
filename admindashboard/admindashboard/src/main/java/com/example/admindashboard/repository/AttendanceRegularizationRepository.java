package com.example.admindashboard.repository;

import com.example.admindashboard.model.AttendanceRegularization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRegularizationRepository extends JpaRepository<AttendanceRegularization, Long> {
    List<AttendanceRegularization> findByStatus(String status); // For Admin Dashboard
}