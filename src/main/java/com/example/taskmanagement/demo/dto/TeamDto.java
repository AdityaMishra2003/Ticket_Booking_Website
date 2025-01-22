package com.example.taskmanagement.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {
    
    private Long teamId;
    private String teamName;
    private List<String> memberDetails;
    private int totalProjects; // New field for total completed projects

    // Getters and Setters
    // ...
    
    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }
}
