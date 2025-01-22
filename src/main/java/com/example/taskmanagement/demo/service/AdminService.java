package com.example.taskmanagement.demo.service;

import com.example.taskmanagement.demo.entity.User;
import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    User getUserByUsername(String username);
    User getUserByEmpId(String empId);
}
