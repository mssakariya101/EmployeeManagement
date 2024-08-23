package com.EmployeeManagement.fileservice;

import com.EmployeeManagement.dto.EmployeeDTO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfService {

    public ByteArrayInputStream generatePdf(List<EmployeeDTO> employees) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Add title
        document.add(new Paragraph("Employee Data Table"));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 3, 4, 4, 3, 3, 4});

        // Add table headers
        Stream.of("ID", "Name", "Email", "Address", "Salary", "Birth Date","Joining Date").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();

            header.setPhrase(new Paragraph(headerTitle));
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });

        // Add rows
        for (EmployeeDTO employee : employees) {
            table.addCell(String.valueOf(employee.getEmployeeId()));
            table.addCell(employee.getEmployeeName());
            table.addCell(employee.getEmployeeEmail());
            table.addCell(employee.getEmployeeAddress());
            table.addCell(String.valueOf(employee.getEmployeeSalary()));

            LocalDate date = employee.getDob().plusDays(1);
            String strDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            table.addCell(strDate);

            LocalDateTime dateTime = employee.getJoiningDate().plusDays(1);
            String strDate2 = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            table.addCell(strDate2);

        }

        document.add(table);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


}
