package com.example.taskmanagement.demo.controller;

import java.security.Principal;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taskmanagement.demo.dto.ProjectWithTasksDTO;
import com.example.taskmanagement.demo.dto.TeamDto;
import com.example.taskmanagement.demo.dto.UserDto;
import com.example.taskmanagement.demo.entity.Messages;
import com.example.taskmanagement.demo.entity.OverallDetails;
import com.example.taskmanagement.demo.entity.Projects;
import com.example.taskmanagement.demo.entity.Task;
import com.example.taskmanagement.demo.entity.Team;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.service.EmailService;
import com.example.taskmanagement.demo.service.MessageService;
import com.example.taskmanagement.demo.service.ProjectService;
import com.example.taskmanagement.demo.service.TaskService;
import com.example.taskmanagement.demo.service.TeamService;
import com.example.taskmanagement.demo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;
    private UserService userService;
    @SuppressWarnings("unused")
    private EmailService emailService;
   
    @Autowired
    private TaskService taskService; 
    @Autowired
    private ProjectService projectService; // Add this line
    @Autowired
    private TeamService teamService; 
    @Autowired
    private MessageService messageService; // Add this line
    @Autowired
    private TaskController taskController;
    
    public UserController(UserService userService, EmailService emailService, TaskService taskService) {
        this.userService = userService;
        this.emailService = emailService;
        this.taskService = taskService; // Initialize it here
    }
    

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model, HttpSession session) {
        User existingUser = userService.findByUsername(userDto.getUsername());
        if (existingUser != null && existingUser.isVerified()) {
            model.addAttribute("Userexist", existingUser);
            return "register";
        }

        // Generate OTP and send email without saving the user
        String otp = userService.generateOtp();
        try {
            userService.sendVerificationEmail(userDto.getUsername(), otp);
        } catch (MessagingException e) {
            e.printStackTrace();
            model.addAttribute("emailError", "Failed to send verification email. Please try again.");
            return "register";
        }
        
        // Store UserDto in session
        session.setAttribute("userDto", userDto);
        return "redirect:/verify-otp?username=" + userDto.getUsername();
    }

    @GetMapping("/verify-otp")
    public String verifyOtp(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("username") String username, @RequestParam("otp") String otp, Model model, HttpSession session) {
        boolean isOtpValid = userService.verifyOtp(username, otp);
        if (isOtpValid) {
            UserDto userDto = (UserDto) session.getAttribute("userDto");
            if (userDto != null) {
                User user = new User();
                user.setUsername(userDto.getUsername());
                user.setPassword(userDto.getPassword());
                user.setFullname(userDto.getFullname());
                user.setEmpId(userDto.getEmpId());
                user.setVerified(true);
                userService.save(user);
                session.removeAttribute("userDto"); // Clean up session
                return "redirect:/register?success";
            } else {
                model.addAttribute("otpError", "User session has expired. Please register again.");
                return "register";
            }
        } else {
            model.addAttribute("username", username);
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "verify-otp";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam("username") String username, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            String otp = userService.generateOtp();
            try {
                userService.sendVerificationEmail(username, otp);
            } catch (MessagingException e) {
                e.printStackTrace();
                model.addAttribute("emailError", "Failed to send verification email. Please try again.");
                return "forgot-password";
            }
            return "redirect:/verify-otp-forgot?username=" + username;
        } else {
            model.addAttribute("usernameError", "Username not found.");
            return "forgot-password";
        }
    }

    @GetMapping("/verify-otp-forgot")
    public String verifyOtpForgot(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "verify-otp-forgot";
    }

    @PostMapping("/verify-otp-forgot")
    public String verifyOtpForgot(@RequestParam("username") String username, @RequestParam("otp") String otp, Model model) {
        boolean isOtpValid = userService.verifyOtp(username, otp);
        if (isOtpValid) {
            return "redirect:/change-password?username=" + username;
        } else {
            model.addAttribute("username", username);
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "verify-otp-forgot";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePasswordSubmit(@RequestParam("username") String username, 
                                        @RequestParam("password") String password, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setPassword(password); // Ensure to encode the password before saving
            userService.save(user);
            model.addAttribute("suscess","Password Changed");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Username not found.");
            return "change-password";
        }
    }

    @GetMapping("/UserDashboard")
    public String UserDashboard(Model model, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String profilePicture = oAuth2User.getAttribute("picture");
            System.out.println("OAuth2 email: " + email); // Debug
            if (email != null) {
                User user = userService.findByUsername(email);
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("dp", profilePicture);
                    List<Task> ongoingTasks = taskService.getOngoingOnlyTasksByUserId(user.getId());
                     model.addAttribute("ongoingTasks", ongoingTasks);
                     List<Task> overdueTasks = taskService.getOverdueTasksByUserId(user.getId());
                     model.addAttribute("overdueTasks", overdueTasks);
                    List<Task> completedTasks = taskService.getCompletedTasksByUserId(user.getId());
                    System.out.println("Completed Tasks: " + completedTasks); 
                    model.addAttribute("completedTasks", completedTasks); // Add completed tasks to the model
                    return "UserDashboard";
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
                // Fetch completed tasks for the user
                List<Task> ongoingTasks = taskService.getOngoingOnlyTasksByUserId(user.getId());
                     model.addAttribute("ongoingTasks", ongoingTasks);
                     List<Task> overdueTasks = taskService.getOverdueTasksByUserId(user.getId());
                     model.addAttribute("overdueTasks", overdueTasks);
                List<Task> completedTasks = taskService.getCompletedTasksByUserId(user.getId());
                model.addAttribute("completedTasks", completedTasks); // Add completed tasks to the model
            }
            return "UserDashboard";
        }
        return "error";
    }
    @GetMapping("/UserMessage")
    public String UserMessage(Authentication authentication, Model model) {
       User user=taskController.getUserFromAuthentication(authentication);
    
       List<Messages> EmailList=messageService.findMessagesBySenderEmailAndRecieverEmail(user.getUsername());
       model.addAttribute("EmailList",EmailList);
        
        return "/UserMessage";
    }
    @GetMapping("/userProjects")
    public String UserProject(Authentication authentication, Model model) {
        User user = taskController.getUserFromAuthentication(authentication);
    
        // Retrieve the list of teams for the authenticated user
        List<Team> teams = teamService.findTeamsByUserId(user.getEmpId());
    
        // Prepare a list to hold projects with tasks for all teams
        List<ProjectWithTasksDTO> allincompleteProjectsWithTasks = new ArrayList<>();
        List<ProjectWithTasksDTO> allCompletedProjectsWithTasks = new ArrayList<>();
    
        // Iterate over each team
        for (Team team : teams) {
            String teamName = team.getTeamName();  // Extract the team name
    
            // Fetch uncompleted projects for this team
            List<Projects> uncompletedProjects = projectService.uncompletedProjectsByteam(teamName);
    
            for (Projects project : uncompletedProjects) {
                List<Task> tasks = taskService.findTasksByProjectId(project.getProjectId());
                if (!tasks.isEmpty()) {
                    ProjectWithTasksDTO projectWithTasks = new ProjectWithTasksDTO(project, tasks);
                    allincompleteProjectsWithTasks.add(projectWithTasks);
                }
            }
    
            // Fetch completed projects for this team
            List<Projects> completedProjects = projectService.completedProjectByteam(teamName);
            for (Projects project : completedProjects) {
                List<Task> tasks = taskService.findTasksByProjectId(project.getProjectId());
                ProjectWithTasksDTO projectWithTasks = new ProjectWithTasksDTO(project, tasks);
                allCompletedProjectsWithTasks.add(projectWithTasks);
            }
        }
    
        model.addAttribute("projectsWithTasks", allincompleteProjectsWithTasks);
        model.addAttribute("completedProjects", allCompletedProjectsWithTasks);
    
        return "/userProjects";
    }
    
    @GetMapping("/UserProfile")
    public String userProfile(Authentication authentication, Model model) {
        User user = taskController.getUserFromAuthentication(authentication);
        
        
        OverallDetails overallDetails = userService.findInOverallDetails(user.getEmpId()); // Use empId for lookup
        
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
        System.out.println(overallDetails);
        int ProjectCount = allCompletedProjectsWithTasks.size();
    
        
        if (overallDetails == null) {
            
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
    
        return "UserProfile"; // Ensure this matches your Thymeleaf template name
    }
    
    
    
    
    @GetMapping("/UserTeam")
public String UserTeamController(Authentication authentication,Model model) {
    User user = taskController.getUserFromAuthentication(authentication);
    List<Team> teams=teamService.findTeamsByUserId(user.getEmpId());
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

    return "/UserTeam"; // Return the view name
}

@GetMapping("/UserPassword")
public String AdminPasswordChange(Model model) {
    return "/UserPassword";
}
@PostMapping("/userChangePassword")
public String ChangePassword(Authentication authentication,
                            @RequestParam("current-password") String curr,
                            @RequestParam("new-password") String newpass,
                            @RequestParam("confirm-password") String confirmpass,Model model) {
 User user = taskController.getUserFromAuthentication(authentication);
 if(newpass.equals(confirmpass)){
    if(newpass.equals(curr)){
        model.addAttribute("errorMessage", "New And Old Password Must be different");
        return "/UserPassword";
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

    
  return "/UserPassword";
}
@PostMapping("/updateProfile")
public String postMethodName(Authentication authentication,
                              @RequestParam(value = "fullName", required = false) String fullName,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "empId", required = false) String empId,
                              @RequestParam(value = "insta", required = false) String instagram,
                              @RequestParam(value = "facebook", required = false) String facebook,
                              @RequestParam(value = "LinkedIn", required = false) String linkedIn,
                              @RequestParam(value = "GitHub", required = false) String gitHub,
                              @RequestParam(value = "Twitter", required = false) String twitter,
                              @RequestParam(value = "WhatsApp", required = false) String whatsapp,
                              @RequestParam(value = "googlePlus", required = false) String googlePlus,
                              Model model) {
    
    User user = taskController.getUserFromAuthentication(authentication);
    
    OverallDetails overallDetails = userService.findInOverallDetails(user.getEmpId());
   
    if(fullName!=null){
        user.setFullname(fullName);
        overallDetails.setFullName(fullName);
    }
    if(email!=null){
        user.setUsername(email);
        overallDetails.setEmail(email);
    }
    if(empId!=null){
        user.setEmpId(empId);
        overallDetails.setEmpId(empId);
    }
    if (instagram != null) {
        overallDetails.setInstagramLink(instagram);
    }
    if (facebook != null) {
        overallDetails.setFacebookLink(facebook);
    }
    if (linkedIn != null) {
        overallDetails.setLinkedinLink(linkedIn);
    }
    if (gitHub != null) {
        overallDetails.setGithubLink(gitHub);
    }
    if (twitter != null) {
        overallDetails.setTwitterLink(twitter);
    }
    if (whatsapp != null) {
        overallDetails.setWhatsappLink(whatsapp);
    }
    if (googlePlus != null) {
        overallDetails.setGooglePlusLink(googlePlus);
    }

    userService.save(user); // Save updated user information
    userService.saveInOverallDetails(overallDetails);

    // Optionally, you can return a success message or redirect to a profile page
    model.addAttribute("successMessage", "Profile updated successfully!");
    return "redirect:/UserProfile"; // Example redirect
}

}
