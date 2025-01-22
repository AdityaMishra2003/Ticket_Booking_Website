package com.example.taskmanagement.demo.dto;

import java.util.List;

import com.example.taskmanagement.demo.entity.Projects;
import com.example.taskmanagement.demo.entity.Task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectWithTasksDTO {
  

    private Projects project;
    private List<Task> tasks;

    public ProjectWithTasksDTO(Projects project, List<Task> tasks) {
        this.project = project;
        this.tasks = tasks;
    }

    // Getters and Setters
    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
      @Override
    public String toString() {
        return "ProjectWithTasksDTO [project=" + project + ", tasks=" + tasks + ", getProject()=" + getProject()
                + ", getTasks()=" + getTasks() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }
}
