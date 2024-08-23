package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.*;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import com.EmployeeManagement.service.DepartmentService;
import com.EmployeeManagement.service.EmployeeService;
import com.EmployeeManagement.service.FilesStorageService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.*;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    FilesStorageService filesStorageService;

    @GetMapping("")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpSession session){
        session.setAttribute("username", userDetails.getUsername());
        model.addAttribute("allDepartment", departmentService.getAllDepartment());
        return "index";
    }


    @GetMapping("/list")
    public ResponseEntity<DataTableResponseDTO> fetchEmployees(
            @ModelAttribute SearchCriteriaDTO searchCriteria,
            @RequestParam(name = "length", defaultValue = "10") int length,
            @ModelAttribute DataTableRequestDTO requestDTO,
            @RequestParam(name = "search", required = false) String search
            ) {
        int page = requestDTO.getStart() / requestDTO.getLength();
        Sort.Direction direction = requestDTO.getSortDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, requestDTO.getLength(), Sort.by(direction, requestDTO.getSortColumn()));

        Page<EmployeeDTO> employeePage = employeeService.filterEmployees(
                searchCriteria.getMinSalary(),
                searchCriteria.getMaxSalary(),
                searchCriteria.getStartDate(),
                searchCriteria.getEndDate(),
                searchCriteria.getJoinStartDate(),
                searchCriteria.getJoinEndDate(),
                searchCriteria.getDepartment(),
                search,
                pageable
        );
        List<EmployeeDTO> employees = employeePage.getContent();
        long totalRecords = employeePage.getTotalElements();

        return new ResponseEntity<>(new DataTableResponseDTO(totalRecords, totalRecords, employees),HttpStatus.OK);

    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("allDepartment", departmentService.getAllDepartment());
        model.addAttribute("employee", new EmployeeDTO());
        return "save-update-employee";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) throws EmployeeDepartmentException {
        model.addAttribute("allDepartment", departmentService.getAllDepartment());
        model.addAttribute("employee",employeeService.getEmployeeById(id));
        return "save-update-employee";
    }
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employee,
                                                    @RequestParam("profile") MultipartFile profile) throws IOException, EmployeeDepartmentException {

        String uniqueFileName = UUID.randomUUID() + "_" + profile.getOriginalFilename();
        employee.setProfileName(uniqueFileName);

        ResponseDTO responseDTO=employeeService.saveEmployee(employee);

        if(responseDTO.getStatus()) {
            filesStorageService.save(profile, uniqueFileName);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employee,
                                                      @RequestParam(value = "profileUpdate" ,required = false) MultipartFile profile) throws EmployeeDepartmentException, IOException {

        if (!profile.isEmpty()) {
            String uniqueFileName = UUID.randomUUID() + "_" + profile.getOriginalFilename();
            employee.setProfileName(uniqueFileName);
            filesStorageService.save(profile, uniqueFileName);
        }
        return new ResponseEntity<>(employeeService.updateEmployee(employee),HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteEmployee(@PathVariable Long id) throws EmployeeDepartmentException {
         return new ResponseEntity<>(employeeService.deleteEmployeeById(id),HttpStatus.OK);
    }

    @GetMapping("/show-departments/{id}")
    public ResponseEntity<List<DepartmentDTO>> showEmployeeDepartment(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeDepartments(id),HttpStatus.OK);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource file = filesStorageService.load(filename);
        MediaType contentType = MediaType.parseMediaType(file.getURL().openConnection().getContentType());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }
}
