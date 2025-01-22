package com.example.taskmanagement.demo.service.Impl;

import java.util.Random;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.demo.dto.UserDto;
import com.example.taskmanagement.demo.entity.OverallDetails;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.entity.UserOtp;
import com.example.taskmanagement.demo.repository.OverallDetailsRepository;
import com.example.taskmanagement.demo.repository.UserOtpRepository;
import com.example.taskmanagement.demo.repository.UserRepository;
import com.example.taskmanagement.demo.service.EmailService;
import com.example.taskmanagement.demo.service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final EmailService emailService;
    private final OverallDetailsRepository overallDetailsRepository;
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
        String subject = "Complete Your Registration: OTP Verification Required";
        String body = "Hi , "+username+"\n\n"
                    + "We are sharing a verification code to access your account. The code is valid for 10 minutes and usable only once.\n\n"
                    + "Once you have verified the code, your registration/forgot settings will be completed immediately. This is to ensure that only you have access to your account.\n\n"
                    + "Your OTP: " + otp + "\n"
                    + "Expires in: 10 minutes\n\n"
                    + "If you encounter any issues during the verification process or have any questions, feel free to reach out to us. We are happy to help!\n\n"
                    + "If you did not request this OTP, please contact our support team immediately at am9982061@gmail.com. We take your account security seriously and are here to assist you with any concerns.\n\n"
                    + "Thank you for choosing Task Management. We look forward to helping you achieve great things with our platform.\n\n"
                    + "Best Regards,\n"
                    + "Team TaskManagement";
        
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


    @Override
    public User findByEmpID(String empId) {
        return userRepository.findByEmpId(empId);
    }
    @Override
    public OverallDetails saveInOverallDetails(OverallDetails overallDetails) {
        return overallDetailsRepository.save(overallDetails);
    }
    @Override
    public OverallDetails findInOverallDetails(String empId) {
        return overallDetailsRepository.findByEmpId(empId);
    }
}
