package com.EmployeeManagement.dto;

import lombok.Data;

@Data
public class SearchCriteriaDTO {
    private Long minSalary;
    private Long maxSalary;
    private String startDate;
    private String endDate;
    private String joinStartDate;
    private String joinEndDate;
    private String department;

}
