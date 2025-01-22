package com.example.taskmanagement.demo.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn; // Import this
import jakarta.persistence.JoinTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    private String taskName;

    @ManyToMany
     @JoinTable(
        name = "task_user",
        joinColumns = @JoinColumn(name = "tasks_task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> user;  // Assuming there's a User class

    private Date dueDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Projects project;


    @Override
    public String toString() {
        return "Task [taskId=" + taskId + ", taskName=" + taskName + ", user=" + user + ", dueDate=" + dueDate
                + ", status=" + status + ", project=" + project + "]";
    } 
   
}
