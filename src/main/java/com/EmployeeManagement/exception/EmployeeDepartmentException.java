package com.EmployeeManagement.exception;

import org.springframework.http.HttpStatus;

public class EmployeeDepartmentException extends Exception{
    private  String message;
    private HttpStatus httpStatus;

    public EmployeeDepartmentException(String message){
        super(message);
        this.message=message;
        this.httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public EmployeeDepartmentException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
    public EmployeeDepartmentException(HttpStatus httpStatus) {
        this.httpStatus=httpStatus;
    }
}
