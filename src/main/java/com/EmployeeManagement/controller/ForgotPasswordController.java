package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.ForgotPasswordDTO;
import com.EmployeeManagement.dto.ResetPasswordDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.exception.UserException;
import com.EmployeeManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ForgotPasswordController {

    private final UserService userService;

    @Autowired
    public ForgotPasswordController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        boolean emailFound = userService.existsByEmail(forgotPasswordDTO.getEmail());

        if (emailFound) {
            return ResponseEntity.ok(new ResponseDTO("Email found", true));
        } else {
            return ResponseEntity.ok(new ResponseDTO("Email not found", false));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws UserException {
            userService.resetPassword(resetPasswordDTO);
            return ResponseEntity.ok(new ResponseDTO("Password reset successfully", true));
    }
}

