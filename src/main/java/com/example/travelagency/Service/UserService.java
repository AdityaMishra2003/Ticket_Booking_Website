package com.example.travelagency.Service;

import com.example.travelagency.Dto.UserDto;
import com.example.travelagency.Entity.User;

import jakarta.mail.MessagingException;

public interface UserService {
    User findByUsername(String username);
    User save(UserDto userDto);
    User save(User user);
    String generateOtp();
    void sendVerificationEmail(String username, String otp) throws MessagingException;
    boolean verifyOtp(String username, String otp);
}
