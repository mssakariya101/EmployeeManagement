package com.EmployeeManagement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name="department")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        Long departmentId;
        String departmentName;
        String hod;

        @ManyToMany(mappedBy = "departments" ,cascade = CascadeType.ALL)
         List<Employee> employees;

}
