package com.example.taskmanagement.demo.entity;

import java.util.List;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teamId;

    private String teamName;

    // This should be a collection of user details
    @ElementCollection
    private List<String> memberDetails;  // Or you can use a custom object if needed

    public Team(String teamName, List<String> memberDetails) {
        this.teamName = teamName;
        this.memberDetails = memberDetails;
    }
}
