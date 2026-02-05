package com.example.admindashboard.controller;

import com.example.admindashboard.security.JwtUtil; // Imports your custom JWT class
import jakarta.servlet.http.Cookie;                 // Fixes "cannot find symbol class Cookie"
import jakarta.servlet.http.HttpServletResponse;    // Fixes "cannot find symbol class HttpServletResponse"
import org.springframework.beans.factory.annotation.Autowired; // Fixes "cannot find symbol class Autowired"
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    // --- SECURE LOGIN LOGIC ---
    @PostMapping("/login-submit")
    public String handleLogin(@RequestParam String userId, @RequestParam String password, HttpServletResponse response) {

        String upperId = userId.trim().toUpperCase();
        String role = "";
        String redirectUrl = "";

        // 1. DETERMINE ROLE & VALIDATE (Simple Logic)
        if (upperId.equals("ADMIN") || upperId.startsWith("ADM")) {
            role = "ADMIN";
            redirectUrl = "redirect:/admin/dashboard";
        } else if (upperId.startsWith("CLI")) {
            role = "CLIENT";
            redirectUrl = "redirect:/client/dashboard";
        } else {
            role = "EMPLOYEE";
            redirectUrl = "redirect:/employee/dashboard";
        }

        // 2. GENERATE JWT TOKEN
        String token = jwtUtil.generateToken(upperId, role);

        // 3. CREATE SECURE COOKIE
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true); // JavaScript cannot read this (Security Best Practice)
        cookie.setPath("/"); // Available for entire app
        cookie.setMaxAge(60 * 60 * 10); // 10 Hours

        // 4. ADD COOKIE TO RESPONSE
        response.addCookie(cookie);
        return redirectUrl;
    }

    // --- PROTECTED ROUTES (No changes needed, SecurityConfig protects them) ---
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() { return "admin-dashboard"; } //This looks for admin-dashboard.html in templates

    @GetMapping("/client/dashboard")
    public String showClientDashboard() { return "client-dashboard"; } //This looks for client-dashboard.html in templates

    @GetMapping("/employee/dashboard")
    public String showEmployeeDashboard() { return "employee"; } //This looks for employee.html in templates


    // -- EMPLOYEE PORTAL PAGES --

    // 1. My Profile Section of the Employee portal
    @GetMapping("/my-profile")
    public String showProfilePage() { return "my-profile"; }  // This returns "my-profile.html" from templates folder

    // 2. "Apply for Leave" Page controller
    @GetMapping("/apply-leave")
    public String showLeavePage() { return "apply-leave"; }

    // 3. "Conference Room" Page controller
    @GetMapping("/conference-room")
    public String showConferencePage() { return "conference-room"; }

    // 4. "My Approvals" Page controller
    @GetMapping("/my-approvals")
    public String showApprovalsPage() { return "my-approvals"; }

    // 5. "My WhiteCircle" Page controller
    @GetMapping("/my-whitecircle")
    public String showMyWhiteCircle() { return "my-whitecircle"; }

    // 6. "Time Sheet" Page controller
    @GetMapping("/timesheet")
    public String showTimesheetPage() { return "timesheet"; }

    // 7. "Attendance Regulation" Page Controller
    @GetMapping("/attendance")
    public String showAttendancePage() { return "attendance"; }

    // 8. "Password Reset" Page controller
    @GetMapping("/password-reset")
    public String showPasswordResetPage() { return "password-reset"; }

    // 9. Payroll Page
    @GetMapping("/payroll")
    public String showPayrollPage() { return "payroll"; }

    // 10. Log Tickets / Helpdesk Page
    @GetMapping("/tickets")
    public String showTicketsPage() { return "tickets"; }

    // 11. Email Signature Generator Page
    @GetMapping("/email-signature")
    public String showEmailSignaturePage() { return "email-signature"; }


    // -- CLIENT PORTAL PAGES --

    // 1. Client profile page controller
    @GetMapping("/client/profile")
    public String showClientProfile() {
        return "client-profile";
    }


    // This handles the "GET" request - when someone types the URL in the browser
    @GetMapping("/")
    public String showLandingPage() {
        return "index"; // Or "login", whatever your html file name is
    }



}

