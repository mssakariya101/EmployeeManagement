package com.EmployeeManagement.fileservice;

import com.EmployeeManagement.dto.EmployeeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelService  {

    public ByteArrayInputStream generateExcel(List<EmployeeDTO> employees) throws IOException {
        String[] columns = {"ID", "Name", "Email", "Address", "Salary", "Birth Date", "Joining Date"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Employees");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (EmployeeDTO employee : employees) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(employee.getEmployeeId());
                row.createCell(1).setCellValue(employee.getEmployeeName());
                row.createCell(2).setCellValue(employee.getEmployeeEmail());
                row.createCell(3).setCellValue(employee.getEmployeeAddress());
                row.createCell(4).setCellValue(employee.getEmployeeSalary());

                LocalDate date = employee.getDob().plusDays(1);
                String strDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                row.createCell(5).setCellValue(strDate);

                LocalDateTime dateTime = employee.getJoiningDate().plusDays(1);
                String strDate2 = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                row.createCell(6).setCellValue(strDate2);
//                row.createCell(6).setCellValue(employee.getProfileUrl());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
