package com.example.admindashboard.controller;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.TimesheetRepository;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @GetMapping("/admin/report/timesheet")
    public String showTimesheetReport(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {

        List<User> employees;

        // --- CHANGED LOGIC HERE ---
        if (search != null && !search.isEmpty()) {
            // Search ONLY among Employees
            employees = userRepository.searchByRole("EMPLOYEE", search);
        } else {
            // Default: Show ONLY Employees (No Admins, No Clients)
            employees = userRepository.findByRoleOrderByUsernameAsc("EMPLOYEE");
        }

        model.addAttribute("employees", employees);

        // (The rest of your code for userId and timesheets stays exactly the same)
        if (userId != null) {
            User selectedUser = userRepository.findById(userId).orElse(null);
            if (selectedUser != null) {
                // Ensure we only show timesheets if the selected user is actually an Employee
                if ("EMPLOYEE".equals(selectedUser.getRole())) {
                    List<Timesheet> employeeTimesheets = timesheetRepository.findByUserOrderByWeekStartDateDesc(selectedUser);
                    model.addAttribute("selectedUser", selectedUser);
                    model.addAttribute("timesheets", employeeTimesheets);
                }
            }
        }

        return "admin-weekly-timesheet-report";
    }
}