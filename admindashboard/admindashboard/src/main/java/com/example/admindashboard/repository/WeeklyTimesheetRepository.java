package com.example.admindashboard.repository;

import com.example.admindashboard.model.WeeklyTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyTimesheetRepository extends JpaRepository<WeeklyTimesheet, Long> {

    // Custom query to find if this employee already started a timesheet for this specific week
    Optional<WeeklyTimesheet> findByEmployeeIdAndWeekStartDate(Long employeeId, LocalDate weekStartDate);

    // Fetch all timesheets for an employee, newest first
    List<WeeklyTimesheet> findByEmployeeIdOrderByWeekStartDateDesc(Long employeeId);
}