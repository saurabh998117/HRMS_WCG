package com.example.admindashboard.controller;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.admindashboard.repository.TimesheetRepository;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    // --- 1. LOGIN PAGE MAPPINGS ---
    // Maps localhost:8080/ to the login page
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    // Maps localhost:8080/login to the login page (REQUIRED by SecurityConfig)
    @GetMapping("/login")
    public String showLoginPage() {
        return "index"; // Looks for index.html
    }

    // --- 2. POST-LOGIN TRAFFIC COP ---
    // SecurityConfig sends successful logins here. We check the role and redirect.

    @GetMapping("/default-redirect")
    public String defaultRedirect(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (request.isUserInRole("CLIENT")) {
            return "redirect:/client/dashboard";
        } else if (request.isUserInRole("EMPLOYEE")) { // Explicit check
            return "redirect:/employee/dashboard";
        }
        return "redirect:/login?error=true";
    }

    // --- PROTECTED ROUTES (No changes made below this line) ---

    // Shows Admin Dashboard and Updated count for Employees and Clients
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        // 1. Fetch live counts from the database
        long totalEmployees = userRepository.countByRole("EMPLOYEE");
        long totalClients = userRepository.countByRole("CLIENT");
        // 2. Send the numbers to the HTML page
        model.addAttribute("empCount", totalEmployees);
        model.addAttribute("clientCount", totalClients);

        return "admin-dashboard";
    }

    @GetMapping("/client/dashboard")
    public String showClientDashboard() { return "client-dashboard"; }

    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard() { return "employee-dashboard"; }

    // --- EMPLOYEE PROFILE SECTION ---
    @GetMapping("/employee/profile")
    public String viewProfile(Model model, Principal principal) {
        // 1. Get the username of the person currently logged in
        String username = principal.getName();

        // 2. Find their data in the database
        User user = userRepository.findByUsername(username).orElse(null);

        // 3. Send this data to the HTML page
        model.addAttribute("user", user);

        // 4. Open the profile page
        return "employee-profile";
    }


    // -- EMPLOYEE PORTAL PAGES --

    @GetMapping("/my-profile")
    public String showProfilePage() { return "my-profile"; }

    @GetMapping("/apply-leave")
    public String showLeavePage() { return "apply-leave"; }

    @GetMapping("/conference-room")
    public String showConferencePage() { return "conference-room"; }

    @GetMapping("/my-approvals")
    public String showApprovalsPage() { return "my-approvals"; }

    @GetMapping("/my-whitecircle")
    public String showMyWhiteCircle() { return "my-whitecircle"; }

    @GetMapping("/timesheet")
    public String showTimesheetPage() { return "timesheet"; }

    @GetMapping("/attendance")
    public String showAttendancePage() { return "attendance"; }

    @GetMapping("/password-reset")
    public String showPasswordResetPage() { return "password-reset"; }

    @GetMapping("/payroll")
    public String showPayrollPage() { return "payroll"; }

    @GetMapping("/tickets")
    public String showTicketsPage() { return "tickets"; }

    @GetMapping("/email-signature")
    public String showEmailSignaturePage() { return "email-signature"; }


    // -- CLIENT PORTAL PAGES --

    @GetMapping("/client/profile")
    public String showClientProfile() {
        return "client-profile";
    }


    // -- ADMIN PORTAL PAGE CONTROLLER--

    // Add New Employee page
    @GetMapping("/admin/add-employee")
    public String showAddEmployeeForm(Model model) {
        // We add a new User object so the form has something to hold the data
        model.addAttribute("user", new User());
        return "add-employee";
    }

    @PostMapping("/admin/add-employee-submit")
    public String addEmployee(@ModelAttribute User user, Model model) {

        // If the ID (username) already exists in the database...
        if (userRepository.existsByUsername(user.getUsername())) {

            // Add the error message to display in the red alert box
            model.addAttribute("errorMessage", "Employee ID already exists. Please use a different ID.");

            // Return to the SAME page (not redirect) so the user doesn't lose their other inputs
            return "add-employee";
        }

        // If ID is unique, proceed with setting defaults and saving
        user.setPassword("{noop}welcome123");

        // CHANGE THIS: From "ROLE_USER" to "EMPLOYEE"
        // (Spring Security adds the ROLE_ prefix automatically)
        user.setRole("EMPLOYEE");
        userRepository.save(user);

        // Redirect to the Employee Report so they can see the new entry immediately
        return "redirect:/admin/reports?type=employee";
    }

    // Timesheet Approval Page
    @GetMapping("/admin/timesheet-approval")
    public String showTimesheetApprovalPage() {
        // FIXED: Added dashes to match admin-timesheet-approval.html
        return "admin-timesheet-approval";
    }

    // Attendance Regularization page
    @GetMapping("/admin/attendance-regularization")
    public String showRegularizationPage() {
        return "admin-attendance-regularization";
    }

    @PostMapping("/admin/timesheets/approve/{id}")
    public String approveTimesheet(@PathVariable Long id, java.security.Principal principal) {
        // 1. Find the timesheet
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid timesheet Id:" + id));

        // 2. Update Status
        timesheet.setStatus("Approved");

        // 3. CAPTURE ADMIN NAME (Fixes the blank "Approved By" column)
        if (principal != null) {
            // This saves "ADM001" or whatever username you logged in with
            timesheet.setApprovedBy(principal.getName());
        } else {
            timesheet.setApprovedBy("Admin");
        }

        // 4. Save to Database
        timesheetRepository.save(timesheet);
        return "redirect:/admin/timesheet-approval";
    }

    @GetMapping("/admin/staff")
    public String showStaffDirectory(Model model, @RequestParam(required = false) String keyword) {
        List<User> staffList;

        if (keyword != null && !keyword.isEmpty()) {
            // Simple search by name if they type in the search bar
            staffList = userRepository.findByRoleAndFullNameContainingIgnoreCase("EMPLOYEE", keyword);
        } else {
            // Default: Show all employees sorted alphabetically
            staffList = userRepository.findByRoleOrderByUsernameAsc("EMPLOYEE");
        }

        model.addAttribute("staffList", staffList);
        model.addAttribute("keyword", keyword); // Keep search term in box
        return "admin-staff";
    }



}