package com.example.admindashboard.controller;

import jakarta.servlet.http.HttpServletRequest; // Added for Role Checking
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

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
        } else {
            // Default to Employee Dashboard for ROLE_USER
            return "redirect:/employee/dashboard";
        }
    }

    // --- PROTECTED ROUTES (No changes made below this line) ---

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() { return "admin-dashboard"; }

    @GetMapping("/client/dashboard")
    public String showClientDashboard() { return "client-dashboard"; }

    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard() { return "employee"; }


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
}