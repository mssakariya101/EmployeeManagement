package com.EmployeeManagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
//@JsonFormat
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDTO {
     String message;
     Boolean status ;
}
