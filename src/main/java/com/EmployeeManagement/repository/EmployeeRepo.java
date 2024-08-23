package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmployeeEmail(String email);
    Employee findByEmployeeName(String name);
    List<Employee> findByDepartmentsDepartmentId(Long departmentId);
    List<Employee> findByDepartmentsDepartmentName(String name);
    List<String> findTop10ByEmployeeNameContainingIgnoreCaseOrEmployeeEmailContainingIgnoreCaseOrEmployeeAddressContainingIgnoreCase(String name, String email, String address);

}


