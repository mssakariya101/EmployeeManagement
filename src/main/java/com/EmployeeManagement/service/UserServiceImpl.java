package com.EmployeeManagement.service;

import com.EmployeeManagement.dto.ResetPasswordDTO;
import com.EmployeeManagement.dto.ResponseDTO;
import com.EmployeeManagement.dto.UserRegistrationDTO;
import com.EmployeeManagement.entity.User;
import com.EmployeeManagement.enums.Role;
import com.EmployeeManagement.exception.UserException;
import com.EmployeeManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseDTO saveUser(UserRegistrationDTO registrationDTO) throws UserException {
        if(userRepository.existsByUsername(registrationDTO.getUsername()) || userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new UserException("Username or Email already exists");
        }
        User user = new User();
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return new ResponseDTO("User registered successfully",true);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
       return userRepository.existsByEmail(email);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws UserException {
        User user = userRepository.findByUsernameOrEmail(resetPasswordDTO.getEmail(), resetPasswordDTO.getEmail());
        if(user == null) {
            throw new UsernameNotFoundException("User not found with email: " + resetPasswordDTO.getEmail());
        }
        String currentPassword = user.getPassword();
        if(!currentPassword.isEmpty() && resetPasswordDTO.getCurrentPassword()!=null && !passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), currentPassword)) {
            throw new UserException("Invalid current password");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username,username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities() // This should return a collection of granted authorities (roles)
        );
    }
}
