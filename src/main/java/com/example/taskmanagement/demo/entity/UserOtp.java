package com.example.taskmanagement.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_otp")
public class UserOtp {
    @Id
    private String username; // Username as ID
    private String otp; // OTP value
}
