package com.EmployeeManagement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@Entity
@Table(name="employee_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        Long employeeId;
        String employeeName;
        String employeeEmail;
        String employeeAddress;
        LocalDateTime joiningDate;
        LocalDate dob;
        Long employeeSalary;
        String profileName;

        @ManyToMany
        @JoinTable(name = "dep_emp",
                   joinColumns = @JoinColumn(name="employeeId"),
                   inverseJoinColumns = @JoinColumn(name = "departmentId"))
        List<Department> departments;

    }

