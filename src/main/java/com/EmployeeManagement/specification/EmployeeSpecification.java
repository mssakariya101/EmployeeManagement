package com.EmployeeManagement.specification;

import com.EmployeeManagement.entity.Employee;

import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class EmployeeSpecification  {

    public static Specification<Employee> hasSalaryBetween(Long minSalary, Long maxSalary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("employeeSalary"), minSalary, maxSalary);
    }

    public static Specification<Employee> hasJoiningDateBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("joiningDate"), date);
    }

    public static Specification<Employee> hasJoiningDateAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("joiningDate"), date);
    }

    public static Specification<Employee> hasJoiningDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("joiningDate"), startDate, endDate);
    }

    public static Specification<Employee> hasDobBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("dob"), date);
    }

    public static Specification<Employee> hasDobAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("dob"), date);
    }

    public static Specification<Employee> hasDobBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("dob"), startDate, endDate);
    }


    public static Specification<Employee> belongsToDepartment(String departmentName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("departments").get("departmentName"), departmentName);
    }

    public static Specification<Employee> containsTextInAttributes(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeEmail")), "%" + keyword.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeName")), "%" + keyword.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeAddress")), "%" + keyword.toLowerCase() + "%")
        );
    }

}
