package com.example.taskmanagement.demo.service;

import java.util.List;

import com.example.taskmanagement.demo.entity.Projects;

public interface ProjectService {
    public long countCompletedProject();
    public List<Projects> OngoingProject();
    public Projects findProject(String projectID);
    public long countTotalProject();
    public List<Projects> uncompletedProjects();
    public Projects saveProjects(Projects projects);
    public List<Projects> completedProject();
    public List<Projects> completedProjectByteam(String team);
    public List<Projects> uncompletedProjectsByteam(String team);
}
