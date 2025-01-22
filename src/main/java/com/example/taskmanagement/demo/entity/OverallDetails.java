package com.example.taskmanagement.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OverallDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long overallId;
    private String fullName;
    private String empId;
    private String email;
    private int completedProjects;
    private int completedTask;

   

    
    private String instagramLink;
    private String facebookLink;
    private String linkedinLink;
    private String githubLink;
    private String twitterLink;
    private String whatsappLink;
    private String googlePlusLink;
}
