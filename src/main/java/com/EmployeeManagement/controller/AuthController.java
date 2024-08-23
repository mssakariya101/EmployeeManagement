package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.dto.UserRegistrationDTO;
import com.EmployeeManagement.exception.UserException;
import com.EmployeeManagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register-user")
    public ResponseEntity<ResponseDTO> registerUserAccount(@RequestBody @Valid UserRegistrationDTO userDto) throws UserException {
        return  new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }
}
