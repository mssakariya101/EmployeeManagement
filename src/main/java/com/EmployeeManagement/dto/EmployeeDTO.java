package com.EmployeeManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {

    private Long employeeId;

    @NotBlank(message = "EmployeeName is required")
    @Size(min = 2, max = 20, message = "Employee Name must be between 2 and 20 characters long")
    private String employeeName;

    @NotBlank(message = "Email is required")
    @Email(regexp = "[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,3}",message = "invalid email address")
    private String employeeEmail;

    @NotBlank(message = "Address is required")
    @Size(min=5,max = 100)
    private String employeeAddress;

    @NotNull(message = "Joining date is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime joiningDate;

    @NotNull(message = "Birth of date is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @NotNull(message="employeeSalary is Required")
    @Max(value = 9999999999L)
    private Long employeeSalary;

    private String profileName;
    private String profileUrl;

    @NotNull(message="department is Required")
    @Size(min=1)
    private List<Long> departments;

    public EmployeeDTO(Long employeeId, String employeeName, String employeeEmail, String employeeAddress,  LocalDateTime joiningDate,LocalDate dob,Long employeeSalary,String profileName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.employeeAddress = employeeAddress;
        this.dob = dob;
        this.joiningDate=joiningDate;
        this.employeeSalary = employeeSalary;
        this.profileName=profileName;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", dob=" + dob +
                ", employeeSalary=" + employeeSalary +
                ", departments=" + departments +
                '}';
    }

}