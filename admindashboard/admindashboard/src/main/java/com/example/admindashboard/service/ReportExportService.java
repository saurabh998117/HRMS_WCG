package com.example.admindashboard.service;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportExportService {

    // --- 1. EXPORT EMPLOYEE MASTER REPORT ---
    public void exportEmployeeReportToExcel(HttpServletResponse response, List<User> employees) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Master Report");

        // Define Headers
        String[] columns = {
                "EMP ID", "Full Name", "Email", "Designation",
                "Department", "Joining Date", "Experience",
                "Contact Number", "Reporting Manager", "Status"
        };

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fill Data Rows
        int rowIdx = 1;
        for (User user : employees) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(user.getUsername()); // EMP ID
            row.createCell(1).setCellValue(user.getFullName());
            row.createCell(2).setCellValue(user.getEmail() != null ? user.getEmail() : "N/A");
            row.createCell(3).setCellValue(user.getDesignation() != null ? user.getDesignation() : "N/A");
            row.createCell(4).setCellValue(user.getBusinessUnit() != null ? user.getBusinessUnit() : "N/A");

            // Date handling
            row.createCell(5).setCellValue(user.getJoiningDate() != null ? user.getJoiningDate().toString() : "N/A");

            row.createCell(6).setCellValue(user.getExperience() != null ? user.getExperience() : "N/A");
            row.createCell(7).setCellValue(user.getMobileNumber() != null ? user.getMobileNumber() : "N/A");
            row.createCell(8).setCellValue(user.getReportingManager() != null ? user.getReportingManager() : "N/A");
            row.createCell(9).setCellValue("Active");
        }

        // Auto-size columns for a professional look
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to Response
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    //---- 2. EXPORT TIMESHEET REPORT
    public void exportTimesheetReportToExcel(HttpServletResponse response, List<Timesheet> timesheets) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Timesheet Report");

        // 1. DEFINE HEADERS (Exact Order)
        String[] columns = {
                "Employee ID",      // Index 0
                "Employee Name",    // Index 1
                "Designation",      // Index 2 (NEW)
                "Week Range",       // Index 3
                "Submitted On",     // Index 4
                "Total Hours",      // Index 5
                "Status",           // Index 6
                "Approved By"       // Index 7 (NEW)
        };

        // 2. CREATE HEADER ROW
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // 3. FILL DATA ROWS (Strict Index Mapping)
        int rowIdx = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Timesheet ts : timesheets) {
            Row row = sheet.createRow(rowIdx++);

            // --- COL 0: Employee ID ---
            row.createCell(0).setCellValue(ts.getUser().getUsername());

            // --- COL 1: Name ---
            row.createCell(1).setCellValue(ts.getUser().getFullName());

            // --- COL 2: Designation (The Missing Piece!) ---
            String designation = ts.getUser().getDesignation();
            row.createCell(2).setCellValue(designation != null && !designation.isEmpty() ? designation : "N/A");

            // --- COL 3: Week Range ---
            // Calculate End Date (Start + 6 days)
            String startDate = ts.getWeekStartDate().format(formatter);
            String endDate = ts.getWeekStartDate().plusDays(6).format(formatter);
            row.createCell(3).setCellValue(startDate + " - " + endDate);

            // --- COL 4: Submitted On ---
            if (ts.getSubmissionDate() != null) {
                row.createCell(4).setCellValue(ts.getSubmissionDate().format(formatter));
            } else {
                row.createCell(4).setCellValue("Not Submitted");
            }

            // --- COL 5: Total Hours ---
            row.createCell(5).setCellValue(ts.getTotalHours() != null ? ts.getTotalHours() : 0.0);

            // --- COL 6: Status ---
            row.createCell(6).setCellValue(ts.getStatus());

            // --- COL 7: Approved By ---
            String approver = ts.getApprovedBy();
            row.createCell(7).setCellValue(approver != null && !approver.isEmpty() ? approver : "-");
        }

        // 4. AUTO-SIZE COLUMNS
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 5. WRITE OUTPUT
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }



    // --- 3. HELPER METHOD: CREATE HEADER STYLE (This fixes the error) ---
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Font settings
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // Background color (Navy Blue)
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Alignment
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }
}