package com.example.taskmanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanagement.demo.entity.OverallDetails;



@Repository
public interface OverallDetailsRepository extends JpaRepository<OverallDetails,Long> {
    OverallDetails findByEmpId(String empId);
}
