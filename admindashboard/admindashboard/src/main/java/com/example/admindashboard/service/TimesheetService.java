package com.example.admindashboard.service;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.TimesheetEntry;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.TimesheetRepository;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // 1. Save or Update a Timesheet (Draft Mode)
    public Timesheet saveDraft(String username, Timesheet incomingData) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");
        User user = userOpt.get();

        List<Timesheet> existingSheets = timesheetRepository.findByUserOrderByWeekStartDateDesc(user);
        Timesheet timesheet = existingSheets.stream()
                .filter(t -> t.getWeekStartDate().equals(incomingData.getWeekStartDate()))
                .findFirst()
                .orElse(new Timesheet());

        if (timesheet.getId() == null) {
            timesheet.setUser(user);
            timesheet.setWeekStartDate(incomingData.getWeekStartDate());
            timesheet.setWeekEndDate(incomingData.getWeekStartDate().plusDays(6));
        }

        if ("Submitted".equalsIgnoreCase(timesheet.getStatus()) || "Approved".equalsIgnoreCase(timesheet.getStatus())) {
            throw new RuntimeException("Cannot edit a submitted timesheet!");
        }
        timesheet.setStatus("Draft");
        timesheet.setComments(null);
        timesheet.setRejectionReason(null);

        // 1. Save Flat Hours
        timesheet.setMondayHours(incomingData.getMondayHours());
        timesheet.setTuesdayHours(incomingData.getTuesdayHours());
        timesheet.setWednesdayHours(incomingData.getWednesdayHours());
        timesheet.setThursdayHours(incomingData.getThursdayHours());
        timesheet.setFridayHours(incomingData.getFridayHours());

        // 2. Auto-Calculate Total Hours instantly
        double total = 0;
        Double[] dailyHours = {
                timesheet.getMondayHours(), timesheet.getTuesdayHours(),
                timesheet.getWednesdayHours(), timesheet.getThursdayHours(), timesheet.getFridayHours()
        };
        for (Double hours : dailyHours) {
            if (hours != null) total += hours;
        }
        timesheet.setTotalHours(total);

        // 3. FIX: Smart-Update "Entries" for the Admin Modal Table (No Duplicates!)
        java.time.LocalDate start = timesheet.getWeekStartDate();

        if (timesheet.getEntries() == null) {
            timesheet.setEntries(new java.util.ArrayList<>());
        } else {
            timesheet.getEntries().clear();
        }

        // Reuse the 'start' variable defined at the top of the method
        for (int i = 0; i < 5; i++) {
            Double currentHours = dailyHours[i];
            if (currentHours != null && currentHours > 0) {
                TimesheetEntry newEntry = new TimesheetEntry();
                // Using 'start' which was already defined earlier in the method
                newEntry.setDate(start.plusDays(i));
                newEntry.setHours(currentHours);
                newEntry.setTaskDescription("Regular Work Hours");
                newEntry.setTimesheet(timesheet);
                timesheet.getEntries().add(newEntry);
            }
        }
        return timesheetRepository.save(timesheet);

    }

    // 2. Submit Timesheet (Finalize)
    public String submitTimesheet(Long timesheetId) {
        Optional<Timesheet> sheetOpt = timesheetRepository.findById(timesheetId);
        if (sheetOpt.isEmpty()) return "Timesheet not found";

        Timesheet sheet = sheetOpt.get();

        // --- NEW FIX: Calculate Total Hours from flat fields ---
        double totalHours = 0;
        totalHours += (sheet.getMondayHours() != null ? sheet.getMondayHours() : 0);
        totalHours += (sheet.getTuesdayHours() != null ? sheet.getTuesdayHours() : 0);
        totalHours += (sheet.getWednesdayHours() != null ? sheet.getWednesdayHours() : 0);
        totalHours += (sheet.getThursdayHours() != null ? sheet.getThursdayHours() : 0);
        totalHours += (sheet.getFridayHours() != null ? sheet.getFridayHours() : 0);

        // Fallback if flat fields are empty but entries exist
        if (totalHours == 0 && sheet.getEntries() != null) {
            for (TimesheetEntry entry : sheet.getEntries()) {
                totalHours += (entry.getHours() != null ? entry.getHours() : 0);
            }
        }

        sheet.setTotalHours(totalHours); // Save calculated total back to the database

        // Logic: Cannot submit empty sheet
        if (totalHours == 0) {
            return "Error: Cannot submit an empty timesheet (0 hours).";
        }
        sheet.setStatus("Submitted");
        timesheetRepository.save(sheet);

        String emailBody = "Employee " + sheet.getUser().getUsername() + " has submitted a timesheet for week: " + sheet.getWeekStartDate();
        emailService.sendSimpleEmail("hr-admin@whitecircle.com", "New Timesheet Submission", emailBody);

        return "Timesheet Submitted Successfully!";
    }

    // 3. Get History for Employee
    public List<Timesheet> getEmployeeTimesheets(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return null;
        return timesheetRepository.findByUserOrderByWeekStartDateDesc(userOpt.get());
    }

    // --- ADMIN FEATURES ---
    // 4. Get all Pending Timesheets for Admin
    public List<Timesheet> getPendingTimesheets() {
        return timesheetRepository.findByStatus("Submitted");
    }

    // 5. Approve Timesheet
    public String approveTimesheet(Long id) {
        Optional<Timesheet> sheetOpt = timesheetRepository.findById(id);
        if (sheetOpt.isEmpty()) return "Timesheet not found";

        Timesheet sheet = sheetOpt.get();
        sheet.setStatus("Approved");
        // In a real app, you would set 'approvedBy' here using the logged-in Admin's ID

        timesheetRepository.save(sheet);
        String emailBody = "Your timesheet for week " + sheet.getWeekStartDate() + " has been APPROVED.";
        // We assume the User entity has an email field. If not, use a dummy email.
        String userEmail = sheet.getUser().getEmail();
        if(userEmail != null) {
            emailService.sendSimpleEmail(userEmail, "Timesheet Approved", emailBody);
        }
        return "Timesheet Approved Successfully!";
    }

    // 6. Reject Timesheet
    public String rejectTimesheet(Long id, String reason) {
        Optional<Timesheet> sheetOpt = timesheetRepository.findById(id);
        if (sheetOpt.isEmpty()) return "Timesheet not found";

        Timesheet sheet = sheetOpt.get();
        sheet.setStatus("Rejected");
        sheet.setComments(reason); // Save the rejection reason

        timesheetRepository.save(sheet);
        String emailBody = "Your timesheet for week " + sheet.getWeekStartDate() + " was REJECTED.\nReason: " + reason;
        String userEmail = sheet.getUser().getEmail();
        if(userEmail != null) {
            emailService.sendSimpleEmail(userEmail, "Action Required: Timesheet Rejected", emailBody);
        }
        return "Timesheet Rejected.";
    }

    // Helper method for Admin Controller to filter by status
    public List<Timesheet> getTimesheetsByStatus(String status) {
        return timesheetRepository.findByStatus(status);
    }
}