package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.DepartmentDTO;
import com.EmployeeManagement.dto.EmployeeDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface DepartmentService {
    boolean saveDepartment(DepartmentDTO departmentDTO) throws EmployeeDepartmentException;
    Long getNoOfDepartment();
    DepartmentDTO getDepartmentById(Long id) throws EmployeeDepartmentException;
    List<DepartmentDTO> getAllDepartment() ;
    boolean updateDepartment(DepartmentDTO departmentDTO) throws EmployeeDepartmentException;
    ResponseDTO deleteDepartmentById(Long id) throws EmployeeDepartmentException;
    List<EmployeeDTO> getDepartmentEmployees(Long id);

    Page<DepartmentDTO> findPaginated(int page, int length, String keyword,String sortColumn,String sortDirection);
}

