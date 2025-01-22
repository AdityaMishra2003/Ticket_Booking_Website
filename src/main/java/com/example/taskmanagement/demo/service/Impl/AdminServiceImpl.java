package com.example.taskmanagement.demo.service.Impl;

import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.repository.AdminRepository;
import com.example.taskmanagement.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<User> getAllUsers() {
        // Fetch all users except the admin
        return adminRepository.findAllExcludingAdmin();
    }

    @Override
    public User getUserByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmpId(String empId) {
        return adminRepository.findByEmpId(empId);
    }
}
