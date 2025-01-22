package com.example.taskmanagement.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanagement.demo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Custom query to count tasks by status
    long countByStatus(String status);
    List<Task> findByUser_IdAndStatus(Long userId, String status);
    List<Task> findByStatus(String status);
    List<Task> findByUser_IdAndStatusIn(Long userId, List<String> statuses);
    long countByUser_IdAndStatus(Long userId, String status);
    List<Task> findByUserIdAndStatusIn(Long userId, List<String> statuses);
    List<Task> findByProject_ProjectId(String projectId);
    List<Task> findByStatusIn(List<String> statuses);
    
}
