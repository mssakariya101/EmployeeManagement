package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.DepartmentDTO;
import com.EmployeeManagement.dto.EmployeeDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface EmployeeService {

    EmployeeDTO getEmployeeById(Long id) throws EmployeeDepartmentException;

    List<EmployeeDTO> getAllEmployee();
    List<EmployeeDTO> getAllEmployees(int pageNo, int pageSize,String sortBy);
    Page<EmployeeDTO> findPaginated(int page, int size,String name,String sortColumn,String sortDirection);
    Page<EmployeeDTO> filterEmployees(Long minSalary, Long maxSalary, String startDate,String endDate,String joinStartDate,String joinEndDate,String department ,String search,Pageable pageable);

    ResponseDTO deleteEmployeeById(Long id) throws EmployeeDepartmentException;
    List<DepartmentDTO> getEmployeeDepartments(Long id);
    ResponseDTO saveEmployee(EmployeeDTO employeeDTO) throws EmployeeDepartmentException;
    ResponseDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeDepartmentException;
}
