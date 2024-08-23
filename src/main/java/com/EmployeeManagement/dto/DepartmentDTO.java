package com.EmployeeManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDTO {

    Long departmentId;
    @NotBlank(message = "Department Name is Required")
    @Size(min=2,max = 15,message = "Department must be between 2 and 15 characters long")
    String departmentName;
    @NotBlank(message = "Hod is Required")
    String hod;
    Long totalSalary;
    List<Long> employees;

    public DepartmentDTO(Long departmentId, String departmentName, String hod) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.hod = hod;
    }
}
