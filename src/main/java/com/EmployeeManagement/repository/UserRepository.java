package com.EmployeeManagement.repository;

import com.EmployeeManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsernameOrEmail(String username,String email);
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}