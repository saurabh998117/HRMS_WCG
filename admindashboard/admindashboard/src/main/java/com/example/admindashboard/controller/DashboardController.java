package com.example.admindashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    // 1. The Entry Point (Login Page)
    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    // 2. The Logic to Check Password (UPDATED: Accepts ANY valid input)
    @PostMapping("/login")
    public String verifyLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {

        // TEMPORARY LOGIC:
        // Instead of checking for "admin", we just check if the fields are NOT empty.
        // Once the Database is added, we will check if "username" exists in the DB.

        if (username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty()) {

            // Input is valid (not empty) -> Let them in!
            return "redirect:/dashboard";

        } else {
            // Input is empty -> Block them.
            model.addAttribute("error", "Username and Password cannot be empty");
            return "index";
        }
    }

    // -- The Employee Portal Controllers --

    @GetMapping("/employee")
    public String showEmployeePortal() {
        return "employee"; // This looks for employee.html
    }

    // 1. My Profile Section of the Employee portal
    @GetMapping("/my-profile")
    public String showProfilePage() {
        // This returns "my-profile.html" from the templates folder
        return "my-profile";
    }

    // 2.  "Back to Home" button
    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard() {
        return "employee"; // This loads employee.html
    }

    // 3. "Apply for Leave" Page controller
    @GetMapping("/apply-leave")
    public String showLeavePage() {
        return "apply-leave";
    }

    // 4. "Conference Room" Page controller
    @GetMapping("/conference-room")
    public String showConferencePage() {
        return "conference-room";
    }

    // 5. "My Approvals" Page controller
    @GetMapping("/my-approvals")
    public String showApprovalsPage() {
        return "my-approvals";
    }

    // 6. "My WhiteCircle" Page controller
    @GetMapping("/my-whitecircle")
    public String showMyWhiteCircle() {
        return "my-whitecircle";
    }

    // 7. "Time Sheet" Page controller
    @GetMapping("/timesheet")
    public String showTimesheetPage() {
        return "timesheet";
    }

    // 8. "Attendance Regulation" Page Controller
    @GetMapping("/attendance")
    public String showAttendancePage() {
        return "attendance";
    }

    // 9. "Password Reset" Page controller
    @GetMapping("/password-reset")
    public String showPasswordResetPage() {
        return "password-reset";
    }

    // 10. Payroll Page
    @GetMapping("/payroll")
    public String showPayrollPage() { return "payroll"; }

    // 11. Log Tickets / Helpdesk Page
    @GetMapping("/tickets")
    public String showTicketsPage() { return "tickets"; }

    // 12. Email Signature Generator Page
    @GetMapping("/email-signature")
    public String showEmailSignaturePage() { return "email-signature"; }

    @GetMapping("/login-submit")
    public String handleLogin(@RequestParam String employeeName, HttpSession session) {
        // Save the name into the session so we can use it on other pages
        session.setAttribute("loggedInUser", employeeName);

        // Redirect to the dashboard
        return "redirect:/employee/dashboard";
    }


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

