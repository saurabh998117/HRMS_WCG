package com.example.admindashboard.repository;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByUserOrderByWeekStartDateDesc(User user);

    // For Admin: Find all submitted timesheets to approve
    List<Timesheet> findByStatus(String status);
}