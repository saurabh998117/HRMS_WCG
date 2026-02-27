package com.example.admindashboard.service;

import com.example.admindashboard.dto.TimesheetRowDTO;
import com.example.admindashboard.dto.TimesheetSubmissionDTO;
import com.example.admindashboard.model.WeeklyTimesheet;
import com.example.admindashboard.model.WeeklyTimesheetEntry;
import com.example.admindashboard.repository.WeeklyTimesheetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeeklyTimesheetService {

    @Autowired
    private WeeklyTimesheetRepository timesheetRepository;

    @Transactional
    public void saveWeeklyTimesheet(TimesheetSubmissionDTO payload) {

        // We use a dummy ID for now until Spring Security is added
        Long currentEmployeeId = 1L;

        // 1. Check if a timesheet already exists for this week and employee
        Optional<WeeklyTimesheet> existingTimesheetOpt =
                timesheetRepository.findByEmployeeIdAndWeekStartDate(currentEmployeeId, payload.getWeekStartDate());

        WeeklyTimesheet timesheet;

        if (existingTimesheetOpt.isPresent()) {
            // UPDATE EXISTING TIMESHEET
            timesheet = existingTimesheetOpt.get();

            // Prevent modifying if already approved
            if ("APPROVED".equals(timesheet.getStatus())) {
                throw new RuntimeException("Cannot edit a timesheet that has already been approved.");
            }

            timesheet.setStatus(payload.getStatus());
            timesheet.setOverallComments(payload.getOverallComments());
            timesheet.setTotalWeekHours(payload.getTotalWeekHours());
            timesheet.setSubmittedAt(LocalDateTime.now());

            // Clear old rows so we can replace them with the newly submitted ones
            timesheet.getEntries().clear();

        } else {
            // CREATE NEW TIMESHEET
            timesheet = new WeeklyTimesheet();

            // FIX: Save the raw ID directly
            timesheet.setEmployeeId(currentEmployeeId);

            timesheet.setWeekStartDate(payload.getWeekStartDate());
            timesheet.setWeekEndDate(payload.getWeekEndDate());
            timesheet.setStatus(payload.getStatus());
            timesheet.setOverallComments(payload.getOverallComments());
            timesheet.setTotalWeekHours(payload.getTotalWeekHours());
            timesheet.setSubmittedAt(LocalDateTime.now());
        }

        // 2. Loop through the DTO rows and add them to the timesheet
        if (payload.getRows() != null) {
            for (TimesheetRowDTO rowDto : payload.getRows()) {
                WeeklyTimesheetEntry entry = new WeeklyTimesheetEntry();

                // FIX: Save the raw IDs directly
                entry.setProjectId(rowDto.getProjectId());
                entry.setTaskId(rowDto.getTaskId());

                entry.setType(rowDto.getType());
                entry.setSunHours(rowDto.getSun());
                entry.setMonHours(rowDto.getMon());
                entry.setTueHours(rowDto.getTue());
                entry.setWedHours(rowDto.getWed());
                entry.setThuHours(rowDto.getThu());
                entry.setFriHours(rowDto.getFri());
                entry.setSatHours(rowDto.getSat());
                entry.setRowTotalHours(rowDto.getRowTotal());
                entry.setComments(rowDto.getDetails());

                // Crucial Step: Link the child to the parent
                entry.setWeeklyTimesheet(timesheet);

                // Add the row
                timesheet.getEntries().add(entry);
            }
        }

        // 3. Save to database
        timesheetRepository.save(timesheet);
    }

    public List<WeeklyTimesheet> getAllMyTimesheets() {
        // TODO: Replace with logged-in user ID from Spring Security later
        Long currentEmployeeId = 1L;
        return timesheetRepository.findByEmployeeIdOrderByWeekStartDateDesc(currentEmployeeId);
    }

    public Optional<WeeklyTimesheet> getTimesheetByWeekStartDate(LocalDate startDate) {
        // TODO: Replace with logged-in user ID from Spring Security later
        Long currentEmployeeId = 1L;
        return timesheetRepository.findByEmployeeIdAndWeekStartDate(currentEmployeeId, startDate);
    }
}