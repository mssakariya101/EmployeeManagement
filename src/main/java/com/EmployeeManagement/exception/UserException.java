package com.EmployeeManagement.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {
    private  String message;
    private HttpStatus httpStatus;

    public UserException(String message){
        super(message);
        this.message=message;
        this.httpStatus= HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
