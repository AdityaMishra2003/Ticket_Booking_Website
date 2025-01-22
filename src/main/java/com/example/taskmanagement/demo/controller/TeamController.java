package com.example.taskmanagement.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.taskmanagement.demo.service.TeamService;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
    @PostMapping("/create")
    public String createTeam(
            @RequestParam("teamName") String teamName,
            @RequestParam("memberIds") String[] memberIds,
            @RequestParam("redirectUrl") String redirectUrl) {
    
        // Convert the String[] to a List if needed
        List<String> memberIdsList = Arrays.asList(memberIds);
    
        // Call your service to create the team
        teamService.createTeam(teamName, memberIdsList);
    
        // Redirect or return view
        return "redirect:" + redirectUrl; // Or wherever you want to redirect after creation
    }
 
    
}
