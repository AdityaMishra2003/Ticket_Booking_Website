package com.example.taskmanagement.demo.entity;

import java.util.Date;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Projects {

    @Id
    private String projectId;

    private String projectName;

    private Date projectDueDate;
    private String status;
    private String team;
    @OneToMany
    private List<Task> tasks;  // A project can have multiple tasks
}
