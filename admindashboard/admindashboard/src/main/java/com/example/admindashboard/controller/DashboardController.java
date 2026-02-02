package com.example.admindashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    // The Entry Point (Login Page)
    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    // --- UNIFIED LOGIN METHOD (Handles Admin & Employee) ---
    @GetMapping("/login-submit")
    public String handleLogin(@RequestParam String userId, @RequestParam String password, HttpSession session) {

        // Normalize input to Uppercase (e.g., "adm001" -> "ADM001")
        String upperId = userId.trim().toUpperCase();

        // A. ADMIN CHECK (Starts with "ADM" or is "ADMIN")
        if (upperId.equals("ADMIN") || upperId.startsWith("ADM")) {
            session.setAttribute("loggedInUser", "Administrator");
            session.setAttribute("userRole", "ADMIN");
            return "redirect:/admin/dashboard";
        }

        // B. EMPLOYEE CHECK (Default for all other IDs)
        else {
            session.setAttribute("loggedInUser", "Anil Yadav"); // Dummy name for now
            session.setAttribute("userRole", "EMPLOYEE");
            return "redirect:/employee/dashboard";
        }
    }

    // --- ADMIN DASHBOARD ROUTE ---
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin-dashboard"; // This looks for admin-dashboard.html in templates
    }

    // -- EMPLOYEE PORTAL ROUTE --
    @GetMapping("/employee")
    public String showEmployeePortal() {
        return "employee"; // This looks for employee.html in templates
    }

    // 1. My Profile Section of the Employee portal
    @GetMapping("/my-profile")
    public String showProfilePage() { return "my-profile"; }  // This returns "my-profile.html" from the templates folder

    // 2.  "Back to Home" button
    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard() { return "employee"; }  // This loads employee.html

    // 3. "Apply for Leave" Page controller
    @GetMapping("/apply-leave")
    public String showLeavePage() { return "apply-leave"; }

    // 4. "Conference Room" Page controller
    @GetMapping("/conference-room")
    public String showConferencePage() { return "conference-room"; }

    // 5. "My Approvals" Page controller
    @GetMapping("/my-approvals")
    public String showApprovalsPage() { return "my-approvals"; }

    // 6. "My WhiteCircle" Page controller
    @GetMapping("/my-whitecircle")
    public String showMyWhiteCircle() { return "my-whitecircle"; }

    // 7. "Time Sheet" Page controller
    @GetMapping("/timesheet")
    public String showTimesheetPage() { return "timesheet"; }

    // 8. "Attendance Regulation" Page Controller
    @GetMapping("/attendance")
    public String showAttendancePage() { return "attendance"; }

    // 9. "Password Reset" Page controller
    @GetMapping("/password-reset")
    public String showPasswordResetPage() { return "password-reset"; }

    // 10. Payroll Page
    @GetMapping("/payroll")
    public String showPayrollPage() { return "payroll"; }

    // 11. Log Tickets / Helpdesk Page
    @GetMapping("/tickets")
    public String showTicketsPage() { return "tickets"; }

    // 12. Email Signature Generator Page
    @GetMapping("/email-signature")
    public String showEmailSignaturePage() { return "email-signature"; }


    // --- CLIENT PORTAL CONTROLLERS ---

    @GetMapping("/client")
    public String handleClientLogin(@RequestParam String clientId, HttpSession session) {

        // Setting a static name for now (Phase 1)
        // In Phase 2, we will fetch the real Company Name from the Database using 'clientId'
        session.setAttribute("loggedInUser", "Global Tech Solutions");
        session.setAttribute("userRole", "CLIENT");
        return "redirect:/client/dashboard";
    }

    // 1. Display the Client Dashboard Page
    @GetMapping("/client/dashboard")
    public String showClientDashboard(HttpSession session) {
        // Optional: Simple security check
        // If no user is logged in, send them back to login page
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }
        return "client-dashboard";
    }

    // 2. Client profile page controller
    @GetMapping("/client/profile")
    public String showClientProfile() {
        return "client-profile";
    }









    //  The Notification Center Route
    @GetMapping("/notifications")
    public String notificationsPage() {
        return "notifications";
    }
}

