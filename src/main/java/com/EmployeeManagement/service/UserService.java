package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.ResetPasswordDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.dto.UserRegistrationDTO;
import com.EmployeeManagement.entity.User;
import com.EmployeeManagement.exception.UserException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseDTO saveUser(UserRegistrationDTO registrationDTO) throws UserException;

    User findUserByEmail(String email);
    User findByUsername(String username);
    Boolean existsByEmail(String email);

    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws UserException;
}
