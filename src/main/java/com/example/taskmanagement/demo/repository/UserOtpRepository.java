package com.example.taskmanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanagement.demo.entity.UserOtp;

public interface UserOtpRepository extends JpaRepository<UserOtp, String> {
}
