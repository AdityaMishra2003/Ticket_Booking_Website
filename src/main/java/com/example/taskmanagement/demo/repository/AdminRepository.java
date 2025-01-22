package com.example.taskmanagement.demo.repository;

import com.example.taskmanagement.demo.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    // This can be empty as JpaRepository provides methods like findAll().
    User findByUsername(String username);

    // Custom method to find a User by employee ID
    User findByEmpId(String empId);

    
    @Query("SELECT u FROM User u WHERE u.username <> 'am9982061@gmail.com'")
    List<User> findAllExcludingAdmin();
}
