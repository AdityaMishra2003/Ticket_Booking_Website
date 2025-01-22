package com.example.taskmanagement.demo.service;


import java.util.List;
import java.util.Optional;
import com.example.taskmanagement.demo.entity.Team;

public interface TeamService {
    public List<Team> getAllTeams();
    public Optional<Team> getTeamById(Long teamId);
    public Team saveTeam(Team team);
    public void deleteTeam(Long teamId) ;
    public Team createTeam(String teamName, List<String> memberIds);
    public long countTeams();
    public List<Team> findTeamsByUserId(String empId);
   
}
