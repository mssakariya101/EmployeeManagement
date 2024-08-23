package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
      boolean existsByDepartmentName(String departmentName);
      List<Department> findByEmployeesEmployeeId(Long employeeId);
      Page<Department> findByDepartmentNameContainingIgnoreCase(String keyword, Pageable pageable);
}
