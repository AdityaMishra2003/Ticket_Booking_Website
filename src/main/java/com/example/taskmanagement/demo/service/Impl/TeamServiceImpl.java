package com.example.taskmanagement.demo.service.Impl;

import com.example.taskmanagement.demo.entity.Team;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.repository.TeamRepository;
import com.example.taskmanagement.demo.repository.UserRepository;
import com.example.taskmanagement.demo.service.TeamService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public long countTeams() {
        return teamRepository.count(); // Use the built-in count() method
    }
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    @Transactional
public Team createTeam(String teamName, List<String> memberIds) {
    // Create a new team instance
    Team team = new Team();
    team.setTeamName(teamName);

    // Find users by empId
    List<User> members = userRepository.findByEmpIdIn(memberIds);
    
    // Prepare a list to store member details in the format you want
    List<String> memberDetails = new ArrayList<>();
    
    for (User user : members) {
        // Extract required data and create a string or custom object representation
        String detail = String.format("TeamName: %s,EmpID: %s, Username: %s, Fullname: %s", 
            teamName,user.getEmpId(), user.getUsername(), user.getFullname());
        
        memberDetails.add(detail);
    }

    // Set the member details in the team
    team.setMemberDetails(memberDetails);

    // Save the team
    return teamRepository.save(team);
}
@Override
public List<Team> findTeamsByUserId(String empId) {
       
    return teamRepository.findTeamsByUserId(empId);

}

}
