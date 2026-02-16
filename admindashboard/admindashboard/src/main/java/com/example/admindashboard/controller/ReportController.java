package com.example.admindashboard.controller;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.TimesheetRepository;
import com.example.admindashboard.repository.UserRepository;
import com.example.admindashboard.service.ReportExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private ReportExportService exportService;

    // TODO: Uncomment when Attendance/Leave Repositories are created
    // @Autowired private AttendanceRepository attendanceRepository;
    // @Autowired private LeaveRepository leaveRepository;

    @GetMapping("/admin/reports")
    public String showReportsDashboard(
            @RequestParam(defaultValue = "employee") String type,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // If the user doesn't pick a date, we default to the current month.
        if (from == null) from = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        if (to == null) to = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        // 1. Add Common Attributes (So filters stick in UI)
        model.addAttribute("currentType", type);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentSortDir", sortDir);
        model.addAttribute("fromDate", from);
        model.addAttribute("toDate", to);
        model.addAttribute("currentPage", page);

        String keyword = (search != null) ? search : "";

        // 2. LOGIC SWITCHER (Routes to the 4 separate pages)
        switch (type) {
            case "employee":
                // 1. DYNAMIC SORTING LOGIC
                Sort sort = sortDir.equalsIgnoreCase("asc") ?
                        Sort.by("username").ascending() :
                        Sort.by("username").descending();

                Pageable empPageable = PageRequest.of(page, size, sort);
                Page<User> employeePage;

                if (search.isEmpty()) {
                    employeePage = userRepository.findByRole("EMPLOYEE", empPageable);
                } else {
                    // This method already searches Name OR Username (EMP ID)
                    employeePage = userRepository.searchEmployees(search, empPageable);
                }
                model.addAttribute("dataPage", employeePage);
                return "admin/employee-master-report";

            case "timesheet":
                // 1. Standard Pagination & Sorting
                Pageable timePageable = PageRequest.of(page, size, Sort.by("weekStartDate").descending());

                // 2. Prepare Keyword
                keyword = (search != null) ? search : "";

                // 3. Fetch Data from Repository
                Page<Timesheet> timesheetPage = timesheetRepository.searchTimesheets(from, to, keyword, timePageable);

                // 4. Send to UI (We will handle the "Week Range" display in the HTML instead)
                model.addAttribute("dataPage", timesheetPage);
                return "admin/timesheets-report";



            case "attendance":
                // return "admin/attendance-report"; // Create this file to avoid errors
                return "redirect:/admin/reports?type=employee"; // Temporary fallback

            case "leave":
                // return "admin/leave-report"; // Create this file to avoid errors
                return "redirect:/admin/reports?type=employee"; // Temporary fallback

            default:
                return "redirect:/admin/reports?type=employee";
        }
    }

    @GetMapping("/admin/reports/download")
    public void downloadReport(
            @RequestParam String type,
            @RequestParam(defaultValue = "excel") String format,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            HttpServletResponse response) throws IOException {

        if (from == null) from = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        if (to == null) to = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + type + "_report.xlsx";
        response.setHeader(headerKey, headerValue);

        if ("employee".equals(type)) {
            List<User> allEmployees = userRepository.findByRoleOrderByUsernameAsc("EMPLOYEE");
            exportService.exportEmployeeReportToExcel(response, allEmployees);
        }
        // Add other export logic here later
    }
}