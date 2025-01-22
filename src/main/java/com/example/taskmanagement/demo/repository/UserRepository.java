package com.example.taskmanagement.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanagement.demo.entity.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User  findByEmpId(String empId);
    boolean existsByUsername(String username);
    List<User> findByEmpIdIn(List<String> empId);
    @SuppressWarnings("null")
    Optional<User> findById(Long id); // Update the return type to Optional<User>

}
