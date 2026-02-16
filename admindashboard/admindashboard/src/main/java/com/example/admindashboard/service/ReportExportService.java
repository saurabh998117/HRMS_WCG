package com.example.admindashboard.service;

import com.example.admindashboard.model.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    // --- 2. HELPER METHOD: CREATE HEADER STYLE (This fixes the error) ---
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