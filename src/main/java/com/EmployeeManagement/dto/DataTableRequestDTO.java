package com.EmployeeManagement.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
public class DataTableRequestDTO {
    int start = 0;
    int length = 10;
    String keyword;
    String sortColumn;
    String sortDirection;
}
