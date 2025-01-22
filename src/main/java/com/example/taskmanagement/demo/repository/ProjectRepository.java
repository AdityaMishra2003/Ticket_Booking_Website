package com.example.taskmanagement.demo.repository;

import java.util.List;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanagement.demo.entity.Projects;

@Repository
public interface ProjectRepository extends JpaRepository<Projects, String> {
    
   
    long countByStatus(String status);
    List<Projects> findByStatus(String status);
    List<Projects> findByStatusIn(List<String> statuses);
    Projects  findByProjectId(String projectId);
    long countByProjectIdInAndStatus(Set<String> projectIds, String status);
    long countByProjectIdInAndStatusIn(Set<String> projectIds, List<String> statuses);
    List<Projects> findByTeamAndStatusIn(String team, List<String> statuses);
    List<Projects> findByTeamAndStatus(String team,String status);
}
