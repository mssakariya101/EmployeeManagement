package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.EmployeeDTO;
import com.EmployeeManagement.fileservice.ExcelService;
import com.EmployeeManagement.fileservice.PdfService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@Controller
public class FileGeneratorController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private ExcelService excelService;


    @PostMapping("/download/pdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestBody List<EmployeeDTO> employees) throws  IOException,DocumentException {

        ByteArrayInputStream bis = pdfService.generatePdf(employees);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @PostMapping("/download/xlsx")
    public ResponseEntity<InputStreamResource> downloadExcel(@RequestBody List<EmployeeDTO> employees) throws IOException {

        ByteArrayInputStream bis = excelService.generateExcel(employees);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }


}
