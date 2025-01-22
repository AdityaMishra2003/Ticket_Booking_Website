package com.example.taskmanagement.demo.service;

import com.example.taskmanagement.demo.dto.UserDto;
import com.example.taskmanagement.demo.entity.OverallDetails;
import com.example.taskmanagement.demo.entity.User;

import jakarta.mail.MessagingException;

public interface UserService {
    User findByUsername(String username);
    User findByEmpID(String empId);
    User save(UserDto userDto);
    User save(User user);
    String generateOtp();
    void sendVerificationEmail(String username, String otp) throws MessagingException;
    boolean verifyOtp(String username, String otp);
    public OverallDetails saveInOverallDetails(OverallDetails overallDetails);
    public OverallDetails findInOverallDetails(String empId);
}
