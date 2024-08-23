package com.EmployeeManagement.controller;


import com.EmployeeManagement.dto.*;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import com.EmployeeManagement.service.DepartmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/view")
    public String viewDepartment(Model model) throws EmployeeDepartmentException {
        model.addAttribute("departments", departmentService.getAllDepartment());
        return "department-list";
    }

    @GetMapping("/chart")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartment(),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<DataTableResponseDTO> getAllDepartment(
            @RequestParam(name="draw") int draw,
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "length", defaultValue = "10") int length,
            @RequestParam(name = "search") String keyword,
            @RequestParam(name="sortColumn") String sortColumn,
            @RequestParam(name="sortDirection") String sortDirection,
            @ModelAttribute DataTableRequestDTO requestDTO) {

        int page = start / length;
//        int page = requestDTO.getStart() / requestDTO.getLength();

        Page<DepartmentDTO> departmentPage ;
        departmentPage=departmentService.findPaginated(page,length,keyword,sortColumn,sortDirection);
//        departmentPage=departmentService.findPaginated(page, requestDTO.getLength(), requestDTO.getKeyword(), requestDTO.getSortColumn(), requestDTO.getSortDirection());

        List<DepartmentDTO> departments = departmentPage.getContent();
        long totalRecords = departmentPage.getTotalElements();

        return new ResponseEntity<>(new DataTableResponseDTO(totalRecords, totalRecords, departments),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveDepartment(@RequestBody DepartmentDTO department) throws EmployeeDepartmentException {
        ResponseDTO response = new ResponseDTO("Department saved successfully!", departmentService.saveDepartment(department));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateDepartment(@RequestBody DepartmentDTO department) throws EmployeeDepartmentException {
        ResponseDTO response = new ResponseDTO("Department updated successfully!", departmentService.updateDepartment( department));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable(value = "id") Long id) throws EmployeeDepartmentException {
        return new ResponseEntity<>(departmentService.getDepartmentById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteDepartmentThroughId(@PathVariable(value = "id") Long id) throws EmployeeDepartmentException {
        return new ResponseEntity<>(departmentService.deleteDepartmentById(id), HttpStatus.OK);

    }

    @GetMapping("/show-employees/{id}")
    public ResponseEntity<List<EmployeeDTO>> showDepartmentEmployees(@PathVariable Long id) {
        return new ResponseEntity<>(departmentService.getDepartmentEmployees(id),HttpStatus.FOUND);
    }
}
