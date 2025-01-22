package com.example.taskmanagement.demo.controller;

import com.example.taskmanagement.demo.dto.ProjectWithTasksDTO;
import com.example.taskmanagement.demo.dto.TeamDto;
import com.example.taskmanagement.demo.entity.Messages;
import com.example.taskmanagement.demo.entity.OverallDetails;
import com.example.taskmanagement.demo.entity.Projects;
import com.example.taskmanagement.demo.entity.Task;
import com.example.taskmanagement.demo.entity.Team;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.service.AdminService;
import com.example.taskmanagement.demo.service.MessageService;
import com.example.taskmanagement.demo.service.ProjectService;
import com.example.taskmanagement.demo.service.TaskService;
import com.example.taskmanagement.demo.service.TeamService;
import com.example.taskmanagement.demo.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskController taskController;
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private TeamService teamService;// Inject TaskService
 
    @Autowired
    private ProjectService projectService; 

    @GetMapping("/user")
    public String listAllUsers(Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "/user";
    }
    
    @GetMapping("/AdminDashboard")
    public String Dashboard(Model model, Principal principal) {
        // Fetch completed tasks count
        long teamCount= teamService.countTeams(); // Fetch the count
        model.addAttribute("teamCount", teamCount); 
        long completedTasksCount = taskService.countCompletedTasks(); // Fetch the count
        model.addAttribute("completedTasksCount", completedTasksCount); // Add to model
        long completedProjectCount = projectService.countCompletedProject(); // Fetch the count
        model.addAttribute("completedProjectCount", completedProjectCount); 

        List<Task> completedTask=taskService.getcompleteTasks();
        model.addAttribute("completedTask",completedTask);

        List<Task> OngoingTasks=taskService.getOngoingTasks();
        model.addAttribute("ongoingTasks",OngoingTasks);

        List<Task> OverdueTasks=taskService.getOverDueTasks();
        model.addAttribute("overdueTasks",OverdueTasks);

        List<Projects> OnGoingProject=projectService.OngoingProject();
        model.addAttribute("OnGoingProject",OnGoingProject);

        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        List<Team> allTeams = teamService.getAllTeams();
        model.addAttribute("teams", allTeams);
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            System.out.println("OAuth2 email: " + email); // Debug
            if (email != null) {
                User user = userService.findByUsername(email);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "AdminDashboard"; // Return view
                } else {
                    model.addAttribute("errorMessage", "User not found.");
                }
            } else {
                model.addAttribute("errorMessage", "OAuth2 email attribute is null.");
            }
        } else {
            if (principal != null) {
                String username = principal.getName();
                System.out.println(username);
                User user = userService.findByUsername(username);
                model.addAttribute("user", user);
            }
            return "AdminDashboard"; // Return view
        }
        return "error";
    }
    @GetMapping("/admin/Reporting")
    public String getReportingPAge(Model model) {
        
        List<Projects> OnGoingProject=projectService.OngoingProject();
        model.addAttribute("OnGoingProject",OnGoingProject);

        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        List<Team> allTeams = teamService.getAllTeams();
        model.addAttribute("teams", allTeams);
    long completedTasksCount = taskService.countCompletedTasks(); // Fetch the count
    model.addAttribute("completedTasksCount", completedTasksCount);

    long ongoingTasksCount = taskService.countOngoingTasks(); // Fetch the count
    model.addAttribute("ongoingTasksCount", ongoingTasksCount); // Add to model

    long overdueTasksCount = taskService.countOverDueTask(); // Fetch the count
    model.addAttribute("overdueTasksCount", overdueTasksCount); // Add to model

    long countTotalTasks = taskService.countTotalTasks(); // Fetch the count
    model.addAttribute("countTotalTasks", countTotalTasks);
    
    long completedProjectCount = projectService.countCompletedProject(); // Fetch the count
    model.addAttribute("completedProjectCount", completedProjectCount);
    long countTotalProject = projectService.countTotalProject(); // Fetch the count
    long notCompletedProjects=countTotalProject-completedProjectCount;
    model.addAttribute("notCompletedProjects", notCompletedProjects); 

    List<Long> completedTasksByMonth = taskService.countCompletedProjectsByMonth();
    model.addAttribute("completedTasksByMonth", completedTasksByMonth); // Add to model
   

    return "AdminReporting";
}
@GetMapping("/AdminProject")
public String AllProjects(Model model) {
    
    List<Projects> OnGoingProject=projectService.OngoingProject();
    model.addAttribute("OnGoingProject",OnGoingProject);

    List<User> allUsers = adminService.getAllUsers();
    model.addAttribute("users", allUsers);
    List<Team> allTeams = teamService.getAllTeams();
    model.addAttribute("teams", allTeams);
    List<Projects> uncompletedProjects = projectService.uncompletedProjects();
    List<ProjectWithTasksDTO> projectsWithTasks = new ArrayList<>();
    
    for (Projects project : uncompletedProjects) {
        List<Task> tasks = taskService.findTasksByProjectId(project.getProjectId());
        if (!tasks.isEmpty()) {  // Only add the project if it has tasks
            ProjectWithTasksDTO projectWithTasks = new ProjectWithTasksDTO(project, tasks);
            projectsWithTasks.add(projectWithTasks);
        }
    }
    List<Projects> completedProjects = projectService.completedProject();
    model.addAttribute("completedProjects", completedProjects);
    model.addAttribute("projectsWithTasks", projectsWithTasks);
    return "/AdminProject";
}
@PostMapping("/updateProjectStatus")
@ResponseBody
public ResponseEntity<String> updateProjectStatus(@RequestBody Map<String, Object> payload) {
    String projectId = (String) payload.get("projectId"); // Corrected to retrieve projectId
    String status = (String) payload.get("status");
    List<Task> tasks = taskService.findTasksByProjectId(projectId);
    for (Task task:tasks){
        task.setStatus(status);
        taskService.saveTask(task);
    }
    // Find the project by projectId
    Projects project = projectService.findProject(projectId);
    if (project != null) {
        // Update the project status
        project.setStatus(status);
        projectService.saveProjects(project); // Save the updated project
        return ResponseEntity.ok("Project status updated successfully");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
    }
}
    @GetMapping("/AdminTask")
    public String adminTaskPage(Model model) {
        
        List<Projects> OnGoingProject=projectService.OngoingProject();
        model.addAttribute("OnGoingProject",OnGoingProject);
        List<Task> ongoingAnddueTask=taskService.findTasksByStatus();
        model.addAttribute("TaskOngoing", ongoingAnddueTask);
        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        List <Task> tasks=taskService.getAlltasks();
        model.addAttribute("AllTasks",tasks);
        List<Team> allTeams = teamService.getAllTeams();
        model.addAttribute("teams", allTeams);
        return "AdminTask";
    }
    @GetMapping("/AdminMessage")
    public String adminMessage(Model model) {
    
    List<Projects> OnGoingProject = projectService.OngoingProject();
    model.addAttribute("OnGoingProject", OnGoingProject);

    List<User> allUsers = adminService.getAllUsers();
    model.addAttribute("users", allUsers);

    List<Team> allTeams = teamService.getAllTeams();
    model.addAttribute("teams", allTeams);
    List<Messages> allUnreadMessages = messageService.findMessagesByStatus();
    model.addAttribute("allunreadMessages", allUnreadMessages); // Fixed typo here
    System.out.println("Unread Messages: " + allUnreadMessages.size());
    return "AdminMessage";
}
@PostMapping("/projects/create")
public String createProject(    @RequestParam("projectId") String id,
                                    @RequestParam("projectName") String projectName,
                                @RequestParam("projectDueDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
                                @RequestParam("teamName") String teamName,
                                @RequestParam("redirectUrl") String redirectUrl // This parameter will help determine the redirection
                            ) {
        Projects projects=new Projects();
        projects.setProjectId(id);
        projects.setProjectName(projectName);
        projects.setProjectDueDate(dueDate);
        projects.setStatus("ongoing");
        projects.setTeam(teamName);
        projectService.saveProjects(projects);
    return "redirect:" + redirectUrl;
}

@GetMapping("/AdminTeam")
public String AdminTeamController(Model model) {
    List<Projects> OnGoingProject = projectService.OngoingProject();
    model.addAttribute("OnGoingProject", OnGoingProject);

    List<User> allUsers = adminService.getAllUsers();
    model.addAttribute("users", allUsers);
    List<Team> teams = teamService.getAllTeams(); // Fetch all teams

    List<TeamDto> tempTeams = new ArrayList<>(); // Use TeamDTO instead of Team

    // Iterate over each team and modify the member details
    for (Team team : teams) {
        List<String> updatedMemberDetails = new ArrayList<>();
        List<Projects> completedProjects = projectService.completedProjectByteam(team.getTeamName());
        int totalProjects = completedProjects.size(); // Get the total projects

        // Iterate through each member's details
        for (String memberDetail : team.getMemberDetails()) {
            String updatedDetail = memberDetail.replaceFirst("TeamName: " + team.getTeamName() + ",", "").trim();
            updatedMemberDetails.add(updatedDetail);
        }

        // Create a new TeamDTO object with the updated member details and total projects
        TeamDto modifiedTeam = new TeamDto();
        modifiedTeam.setTeamId(team.getTeamId());
        modifiedTeam.setTeamName(team.getTeamName());
        modifiedTeam.setMemberDetails(updatedMemberDetails);
        modifiedTeam.setTotalProjects(totalProjects); // Set total projects

        // Add the modified team to the temp list
        tempTeams.add(modifiedTeam);
    }

    model.addAttribute("teams", tempTeams);

    return "/AdminTeam"; // Return the view name
}
@PostMapping("/deleteTeam")
public ResponseEntity<Void> deleteTeam(@RequestBody Map<String, Long> requestBody) {
    Long teamId = requestBody.get("teamId");
    System.out.println("Deleting team with ID: " + teamId);
    teamService.deleteTeam(teamId);
    return ResponseEntity.noContent().build(); // Return HTTP 204 No Content
}

    @GetMapping("/AdminPassword")
    public String AdminPasswordChange(Model model) {
        List<Projects> OnGoingProject = projectService.OngoingProject();
        model.addAttribute("OnGoingProject", OnGoingProject);
    
        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
    
        List<Team> allTeams = teamService.getAllTeams();
        model.addAttribute("teams", allTeams);
        return "/AdminPassword";
        
    }
    @PostMapping("/ChangePassword")
    public String ChangePassword(Authentication authentication,
                                @RequestParam("current-password") String curr,
                                @RequestParam("new-password") String newpass,
                                @RequestParam("confirm-password") String confirmpass,Model model) {
     User user = taskController.getUserFromAuthentication(authentication);
     if(newpass.equals(confirmpass)){
        if(newpass.equals(curr)){
            model.addAttribute("errorMessage", "New And Old Password Must be different");
            return "/AdminPassword";
        }
        if(user.getPassword().equals(curr)){
            user.setPassword(newpass);
            userService.save(user);
            model.addAttribute("successMessage", "Password Change Successful");
        } else {
            model.addAttribute("errorMessage", "Incorrect old Password");
        }
    } else {
        model.addAttribute("errorMessage", "New password and confirm password not matched");
    }
    
        
      return "/AdminPassword";
    }
      @GetMapping("/AdminProfile")
    public String userProfile(Authentication authentication, Model model) {
        User user = taskController.getUserFromAuthentication(authentication);
        List<Projects> OnGoingProject = projectService.OngoingProject();
        model.addAttribute("OnGoingProject", OnGoingProject);
    
        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        // Retrieve existing overall details
        OverallDetails overallDetails = userService.findInOverallDetails(user.getEmpId()); // Use empId for lookup
        
        // Get completed tasks for the user
        Long  completedTasksCount = taskService.countCompletedTasks(); // Fetch the count
        int TaskCount=completedTasksCount.intValue();
        // Find teams associated with the user
        long completedProjectCount = projectService.countCompletedProject();
        int ProjectCount=(int)completedProjectCount;
        List<Team> allTeams = teamService.getAllTeams();
        model.addAttribute("teamsCount", allTeams.size());
        
        if (overallDetails == null) {
            // Create and save new OverallDetails if not present
            overallDetails = new OverallDetails();
            overallDetails.setEmail(user.getUsername());
            overallDetails.setEmpId(user.getEmpId());
            overallDetails.setCompletedTask(TaskCount);
            overallDetails.setCompletedProjects(ProjectCount);
            overallDetails.setFullName(user.getFullname());
    
            userService.saveInOverallDetails(overallDetails);
        } else {
            // Update existing overallDetails if found
            overallDetails.setCompletedTask(TaskCount);
            overallDetails.setCompletedProjects(ProjectCount);
            userService.saveInOverallDetails(overallDetails); // Save updated details
        }
    
        
        model.addAttribute("profileDetails", overallDetails);
    
        return "AdminProfile"; // Ensure this matches your Thymeleaf template name
    }
    @GetMapping("/AdminPortfolio")
    public String AdminPortfolio(Model model) {
        // Fetch all users
        List<User> users = adminService.getAllUsers();
        List<OverallDetails> overallDetailsList = new ArrayList<>(); // To store all users' overall details
        
        // Iterate over each user
        for (User user : users) {
            // Find the overall details for the current user
            OverallDetails overallDetails = userService.findInOverallDetails(user.getEmpId());
            
            // Get completed tasks for the user
            List<Task> completedTasks = taskService.getCompletedTasksByUserId(user.getId());
            int TaskCount = completedTasks.size();
            
            // Find teams associated with the user
            List<Team> teams = teamService.findTeamsByUserId(user.getEmpId());
            List<ProjectWithTasksDTO> allCompletedProjectsWithTasks = new ArrayList<>();
            
            // Iterate over each team to get completed projects
            for (Team team : teams) {
                String teamName = team.getTeamName();
                List<Projects> completedProjects = projectService.completedProjectByteam(teamName);
                for (Projects project : completedProjects) {
                    List<Task> tasks = taskService.findTasksByProjectId(project.getProjectId());
                    ProjectWithTasksDTO projectWithTasks = new ProjectWithTasksDTO(project, tasks);
                    allCompletedProjectsWithTasks.add(projectWithTasks);
                }
            }
    
            // Calculate total completed projects
            int ProjectCount = allCompletedProjectsWithTasks.size();
    
            // If overall details are not present, create and save new record
            if (overallDetails == null) {
                overallDetails = new OverallDetails();
                overallDetails.setEmail(user.getUsername());
                overallDetails.setEmpId(user.getEmpId());
                overallDetails.setCompletedTask(TaskCount);
                overallDetails.setCompletedProjects(ProjectCount);
                overallDetails.setFullName(user.getFullname());
                userService.saveInOverallDetails(overallDetails); // Save the new overall details
            } else {
                // Update existing overallDetails if found
                overallDetails.setCompletedTask(TaskCount);
                overallDetails.setCompletedProjects(ProjectCount);
                userService.saveInOverallDetails(overallDetails); // Save updated details
            }
    
            // Add the overall details of the current user to the list
            overallDetailsList.add(overallDetails);
        }
        List<Projects> OnGoingProject = projectService.OngoingProject();
        model.addAttribute("OnGoingProject", OnGoingProject);
    
        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        // Pass the list of all users' overall details to the frontend
        model.addAttribute("overallDetailsList", overallDetailsList);
        
        return "/AdminPortfolio"; // Return the view for the portfolio page
    }
    
}
