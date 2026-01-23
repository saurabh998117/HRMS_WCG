package com.example.admindashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    // 1. The Entry Point (Login Page)
    @GetMapping("/")
    public String loginPage() {
        return "index"; // Loads index.html
    }

    // 2. The Admin Dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Loads dashboard.html
    }

    // 3. The Client Dashboard
    @GetMapping("/client")
    public String clientDashboard() {
        return "client-dashboard"; // Loads client-dashboard.html
    }
}


