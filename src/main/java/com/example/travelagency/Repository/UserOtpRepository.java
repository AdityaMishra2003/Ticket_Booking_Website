package com.example.travelagency.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.travelagency.Entity.UserOtp;

public interface UserOtpRepository extends JpaRepository<UserOtp, String> {
}
