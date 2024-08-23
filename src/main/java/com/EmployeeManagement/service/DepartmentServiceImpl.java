package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.DepartmentDTO;
import com.EmployeeManagement.dto.EmployeeDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.entity.Department;
import com.EmployeeManagement.entity.Employee;
import com.EmployeeManagement.exception.EmployeeDepartmentException;
import com.EmployeeManagement.repository.DepartmentRepo;
import com.EmployeeManagement.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    EmployeeRepo employeeRepo;

    @Override
    public DepartmentDTO getDepartmentById(Long id) throws EmployeeDepartmentException {
            return convertToDto(getById(id));
    }

    @Override
    public List<DepartmentDTO> getAllDepartment() {
        return convertToDtoList(departmentRepo.findAll());
    }

    @Override
    public List<EmployeeDTO> getDepartmentEmployees(Long id) {
        List<Employee> employees = employeeRepo.findByDepartmentsDepartmentId(id);
        return EmployeeServiceImpl.convertToDtoList(employees);
    }

    @Override
    public Page<DepartmentDTO> findPaginated(int page, int length, String keyword, String sortColumn, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, length, Sort.by(direction, sortColumn));
        Page<Department> departmentPage = departmentRepo.findByDepartmentNameContainingIgnoreCase(keyword,pageable);

        return departmentPage.map(DepartmentServiceImpl::convertToDto);
    }

    @Override
    public boolean saveDepartment(DepartmentDTO departmentDTO) throws EmployeeDepartmentException {
        if (!departmentRepo.existsByDepartmentName(departmentDTO.getDepartmentName())) {
            return saveOrUpdateDepartment(departmentDTO);
        }
        return false;
    }

    @Override
    public boolean updateDepartment(DepartmentDTO departmentDTO) throws EmployeeDepartmentException {
            return saveOrUpdateDepartment(departmentDTO);
    }

    private boolean saveOrUpdateDepartment(DepartmentDTO departmentDTO) throws EmployeeDepartmentException {
        Department department;
        if(departmentDTO.getDepartmentId()==null){
             department=new Department();
        }else{
             department=getById(departmentDTO.getDepartmentId());
        }
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setHod(departmentDTO.getHod());
        departmentRepo.save(department);
        return true;
    }

    @Override
    public ResponseDTO deleteDepartmentById(Long id) throws EmployeeDepartmentException {
        Department department=getById(id);
        if(department.getEmployees().isEmpty()) {
            departmentRepo.delete(department);
            return new ResponseDTO("Department deleted successfully",true);
        }
        else
            throw new EmployeeDepartmentException("Department has employees");
    }

    private Department getById(Long id) throws EmployeeDepartmentException {
        return departmentRepo.findById(id).orElseThrow(() -> new EmployeeDepartmentException("Department not found for id ::" + id));
    }

    public static DepartmentDTO convertToDto(Department department) {
        DepartmentDTO dto = new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName(), department.getHod());
        if (department.getEmployees() != null) {
            dto.setEmployees(department.getEmployees().stream().map(Employee::getEmployeeId).collect(Collectors.toList()));
            long totalSalary = department.getEmployees().stream()
                    .mapToLong(Employee::getEmployeeSalary)
                    .sum();
            dto.setTotalSalary(totalSalary);
        }
        return dto;
    }

    public static List<DepartmentDTO> convertToDtoList(List<Department> departments) {
        return departments.stream()
                .map(DepartmentServiceImpl::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getNoOfDepartment() {
        return (Long) departmentRepo.count();
    }

}
