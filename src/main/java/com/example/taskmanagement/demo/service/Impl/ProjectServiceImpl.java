package com.example.taskmanagement.demo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.demo.entity.Projects;
import com.example.taskmanagement.demo.repository.ProjectRepository;
import com.example.taskmanagement.demo.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService{
     @Autowired
    private ProjectRepository projectRepository;

    // Method to count completed tasks
    public long countCompletedProject() {
        return projectRepository.countByStatus("completed"); // Use the appropriate status
    }
    public long countTotalProject() {
        return projectRepository.count(); // Use the appropriate status
    }
    public List<Projects> OngoingProject() {
        return projectRepository.findByStatus("ongoing"); // Use the appropriate status
    }
    public List<Projects> completedProject() {
        return projectRepository.findByStatus("completed"); // Use the appropriate status
    }
    public Projects findProject(String projectID){
        return projectRepository.findByProjectId(projectID);
    }
    public List<Projects> uncompletedProjects() {
        return projectRepository.findByStatusIn(List.of("ongoing", "overdue")); // Use the appropriate status
    }
    public Projects saveProjects(Projects projects){
        return projectRepository.save(projects);
    }
    public List<Projects> uncompletedProjectsByteam(String team) {
        return projectRepository.findByTeamAndStatusIn(team,List.of("ongoing", "overdue")); // Use the appropriate status
    }
    public List<Projects> completedProjectByteam(String team) {
        return projectRepository.findByTeamAndStatus(team,"completed"); // Use the appropriate status
    }
}
