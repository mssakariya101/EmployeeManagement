package com.EmployeeManagement.service;

import com.EmployeeManagement.controller.EmployeeController;
import com.EmployeeManagement.dto.DepartmentDTO;
import com.EmployeeManagement.dto.EmployeeDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.entity.Employee;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import com.EmployeeManagement.repository.DepartmentRepo;
import com.EmployeeManagement.repository.EmployeeRepo;
import com.EmployeeManagement.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Override
    public EmployeeDTO getEmployeeById(Long id) throws EmployeeDepartmentException {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeDepartmentException("Employee not found for id :: " + id, HttpStatus.NOT_FOUND));

        List<Department> departments = departmentRepo.findByEmployeesEmployeeId(id);
        employee.setDepartments(departments);
        return convertToDto(employee);
    }


    @Override
    public List<DepartmentDTO> getEmployeeDepartments(Long id){
        List<Department> departments = departmentRepo.findByEmployeesEmployeeId(id);
        if(departments.isEmpty())
            return new ArrayList<>();
        return DepartmentServiceImpl.convertToDtoList(departments);
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        List<Employee> employees = employeeRepo.findAll();
        return convertToDtoList(employees);
    }

    public List<EmployeeDTO> getAllEmployees(int pageNo, int pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Employee> pagedResult = employeeRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return convertToDtoList(pagedResult.getContent());
        } else {
            return new ArrayList<EmployeeDTO>();
        }
    }


    @Override
    public Page<EmployeeDTO> findPaginated(int page, int size, String query, String sortColumn, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortColumn));
        Specification<Employee> spec = Specification.where(null);

        return employeeRepo.findAll(spec, pageable).map(EmployeeServiceImpl::convertToDto);
    }

    @Override
    public Page<EmployeeDTO> filterEmployees(Long minSalary, Long maxSalary,
                                             String startDate, String endDate,
                                             String joinStartDate, String joinEndDate,
                                             String department, String search, Pageable pageable) {

        Specification<Employee> spec = Specification.where(null);
        if(maxSalary ==null)
            maxSalary=Long.MAX_VALUE;
        if(minSalary == null)
            minSalary=Long.MIN_VALUE;
        spec=spec.and(EmployeeSpecification.hasSalaryBetween(minSalary, maxSalary));
        if(!(startDate.isEmpty() && endDate.isEmpty()))
               spec=spec.and(EmployeeSpecification.hasDobBetween(parseDate(startDate),parseDate(endDate)));
        if(!(joinStartDate.isEmpty() && joinEndDate.isEmpty()))
            spec=spec.and(EmployeeSpecification.hasJoiningDateBetween(parseDate(joinStartDate).atStartOfDay(),parseDate(joinEndDate).atStartOfDay()));
        if(!department.isEmpty() )
            spec=spec.and(EmployeeSpecification.belongsToDepartment(department));
        if(!search.isEmpty())
            spec=spec.and(EmployeeSpecification.containsTextInAttributes(search));

        return employeeRepo.findAll(spec, pageable).map(EmployeeServiceImpl::convertToDto);
    }


    @Override
    public ResponseDTO saveEmployee(EmployeeDTO employeeDTO) throws EmployeeDepartmentException {
        if (!employeeRepo.existsByEmployeeEmail(employeeDTO.getEmployeeEmail())) {
            if(saveOrUpdateEmployee(employeeDTO, new Employee()))
                return new ResponseDTO("Employee saved successfully",true);
            else
                throw new EmployeeDepartmentException("Error while saving employee");
        }
        throw new EmployeeDepartmentException("Employee already exists");
    }

    @Override
    public ResponseDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeDepartmentException {
        Employee employee = employeeRepo.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeDepartmentException("Employee not found for id :: " + employeeDTO.getEmployeeId(), HttpStatus.NOT_FOUND));
        if(saveOrUpdateEmployee(employeeDTO, employee))
           return new ResponseDTO("Employee Update successfully",true);
        else
            throw new EmployeeDepartmentException("Error while updating employee");
    }

    @Override
    public ResponseDTO deleteEmployeeById(Long id) throws EmployeeDepartmentException {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeDepartmentException("Employee not found for id :: " + id, HttpStatus.NOT_FOUND));
        employeeRepo.delete(employee);
        return new ResponseDTO("Employee Delete successfully",true);
    }
    private boolean saveOrUpdateEmployee(EmployeeDTO employeeDTO, Employee employee) {
        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setEmployeeEmail(employeeDTO.getEmployeeEmail());
        employee.setEmployeeAddress(employeeDTO.getEmployeeAddress());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setDob(employeeDTO.getDob());
        employee.setEmployeeSalary(employeeDTO.getEmployeeSalary());
        employee.setProfileName(employeeDTO.getProfileName());


        if (employeeDTO.getDepartments() != null) {
            List<Department> departments = employeeDTO.getDepartments().stream()
                    .map(departmentId -> {
                        return departmentRepo.findById(departmentId).orElse(null);
                    })
                    .collect(Collectors.toList());
            employee.setDepartments(departments);
        }
        employeeRepo.save(employee);
        return true;
    }


    public static EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getEmployeeId(), employee.getEmployeeName(), employee.getEmployeeEmail(),
                employee.getEmployeeAddress(),employee.getJoiningDate(),employee.getDob(), employee.getEmployeeSalary(), employee.getProfileName());

        String url = MvcUriComponentsBuilder
                .fromMethodName(EmployeeController.class, "getImage",employee.getProfileName()).build().toString();

        employeeDTO.setProfileUrl(url);
        if (employee.getDepartments() != null) {
            employeeDTO.setDepartments(employee.getDepartments().stream().map(Department::getDepartmentId).collect(Collectors.toList()));
        }
        return employeeDTO;
    }

    public static List<EmployeeDTO> convertToDtoList(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeServiceImpl::convertToDto)
                .collect(Collectors.toList());
    }
    private static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("d-M-yyyy");
        return  LocalDate.parse(dateString,formatter);
    }

}
