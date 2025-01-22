package com.example.travelagency.Service.Impl;

import java.util.Random;
import org.springframework.stereotype.Service;
import com.example.travelagency.Dto.UserDto;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Entity.UserOtp;
import com.example.travelagency.Repository.UserRepository;
import com.example.travelagency.Repository.UserOtpRepository;
import com.example.travelagency.Service.EmailService;
import com.example.travelagency.Service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final EmailService emailService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), // No password encoding
        userDto.getFullname(), userDto.isVerified());
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void sendVerificationEmail(String username, String otp) throws MessagingException {
        String subject = "Email Verification";
        String body = "Dear "+username+",\nYour Verification OTP is: " + otp;
        emailService.sendEmailOtp(username, subject, body);

        // Save OTP to `user_otp` table
        UserOtp userOtp = new UserOtp();
        userOtp.setUsername(username);
        userOtp.setOtp(otp);
        userOtpRepository.save(userOtp);
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        UserOtp userOtp = userOtpRepository.findById(username).orElse(null);
        if (userOtp != null && userOtp.getOtp().equals(otp)) {
            userOtpRepository.delete(userOtp); // Remove OTP after successful verification
            return true;
        }
        return false;
    }
}
