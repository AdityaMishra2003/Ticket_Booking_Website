package com.example.taskmanagement.demo.controller;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taskmanagement.demo.entity.Projects;
import com.example.taskmanagement.demo.entity.Task;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.service.EmailService;
import com.example.taskmanagement.demo.service.ProjectService;
import com.example.taskmanagement.demo.service.TaskService;
import com.example.taskmanagement.demo.service.UserService; // Assuming you have a UserService to fetch users

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserService userService; // Service to fetch user details
    private ProjectService projectService;
    public TaskController(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService=projectService;
        this.taskService = taskService; // Initialize it here
    }
     public User getUserFromAuthentication(Authentication authentication) {
        String username = null;
        User user = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            username = (String) attributes.get("email");
            if (username == null) {
                username = (String) attributes.get("login");
            }
            System.out.println("User logged in via OAuth2: " + attributes);
            user = userService.findByUsername(username);
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            System.out.println("User logged in via traditional login: " + username);
            user = userService.findByUsername(username);
        }

        return user;
    }
    @PostMapping("/tasks/save")
    public String saveTask(
            @RequestParam("task-name") String taskName,
            @RequestParam("employee-select") List<String> employeeIds,
            @RequestParam("projectId") String projectId,
            @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
            @RequestParam("redirectUrl") String redirectUrl,
            Model model) {
    
        // Create a new Task
        Task task = new Task();
        task.setTaskName(taskName);
        task.setDueDate(dueDate);
        task.setStatus("ongoing"); // Set initial status
    
        // Set the project for the task
        Projects project = projectService.findProject(projectId);
        task.setProject(project);
    
        // Fetch and assign users (employees) to the task
        List<User> users = new ArrayList<>();
        
        for (String empId : employeeIds) {
            User user = userService.findByEmpID(empId);
            if (user != null) {
                users.add(user);
    
                // Personalized email content
                String subject = "Task Assignment Alert: " + taskName;
                String body = String.format("""
                        Dear %s,
    
                        A new task has been assigned to you by your manager. Below are the details:
    
                        *Task Details:*
                        - Task Name: %s
                        - Project Name: %s
                        - Assigned By: Admin
                        - Due Date: %s
                        - Additional Notes: Please prioritize this task.
    
                        For any clarifications or further guidance, feel free to reply to this email.
    
                        Best regards,
                        Your Management Team
                        """, user.getFullname(), taskName,project.getProjectName(), dueDate);
    
                // Send email to the employee
                emailService.sendTaskDetails("am9982061@gmail.com",user.getUsername(), subject, body);
            }
        }
    
        task.setUser(users); // Set the list of users to the task
    
        // Save the task
        taskService.saveTask(task); // Ensure saveTask method is implemented correctly
        model.addAttribute("successMessage", "Task Added Suscessfully !");

        return "redirect:"+redirectUrl; // Redirect to tasks page after saving
    }   
    @PostMapping("/AdminTasks/save")
    public String saveTaskInAdminTask(
            @RequestParam("task-name") String taskName,
            @RequestParam("employee-select") List<String> employeeIds,
            @RequestParam("projectId") String projectId,
            @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
            RedirectAttributes redirectAttributes) {
    
        // Create a new Task
        Task task = new Task();
        task.setTaskName(taskName);
        task.setDueDate(dueDate);
        task.setStatus("ongoing"); // Set initial status
    
        // Set the project for the task
        Projects project = projectService.findProject(projectId);
        task.setProject(project);
    
        // Fetch and assign users (employees) to the task
        List<User> users = new ArrayList<>();
        for (String empId : employeeIds) {
            User user = userService.findByEmpID(empId);
            if (user != null) {
                users.add(user);
    
                // Personalized email content
                String subject = "Task Assignment Alert: " + taskName;
                String body = String.format("""
                        Dear %s,
    
                        A new task has been assigned to you by your manager. Below are the details:
    
                        *Task Details:*
                        - Task Name: %s
                        - Under Project: %s
                        - Assigned By: Admin
                        - Due Date: %s
                        - Additional Notes: Please prioritize this task.
    
                        For any clarifications or further guidance, feel free to reply to this email.
    
                        Best regards,
                        Your Management Team
                        """, user.getUsername(), taskName,project.getProjectName(), dueDate);
    
                // Send email to the employee
                emailService.sendTaskDetails("am9982061@gmail.com",user.getUsername(), subject, body);
            }
        }
    
        task.setUser(users); // Assign users to the task
    
        // Save the task
        taskService.saveTask(task);
    
        // Success message
        redirectAttributes.addFlashAttribute("successMessage", "Task saved successfully!");
    
        return "redirect:/AdminTask"; // Redirect to admin task page
    }
    
    

    @PostMapping("/updateTaskStatus")
@ResponseBody
public ResponseEntity<String> updateTaskStatus(@RequestBody Map<String, Object> payload) {
    Long taskId = ((Number) payload.get("taskId")).longValue();
    String status = (String) payload.get("status");

    // Find the task by taskId
    Task task = taskService.getTaskById(taskId);
    if (task != null) {
        // Update the task status
        task.setStatus(status);
        taskService.saveTask(task); // Save the updated task
        return ResponseEntity.ok("Task status updated successfully");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }
   
}

@GetMapping("/user/Reporting")
public String getReportingPage(Authentication authentication, Model model) {
    User user = getUserFromAuthentication(authentication);
    
    
    long completedTasksCount = taskService.CountCompletedTasksByUserId(user.getId()); // Fetch the count
    System.out.println("CompletedTasks:"+completedTasksCount);

    model.addAttribute("completedTasksCount", completedTasksCount);

    long ongoingTasksCount = taskService.CountOngoingTasksByUserId(user.getId()); // Fetch the count
    model.addAttribute("ongoingTasksCount", ongoingTasksCount); // Add to model

    long overdueTasksCount = taskService.CountOverdueTasksByUserId(user.getId()); // Fetch the count
    model.addAttribute("overdueTasksCount", overdueTasksCount); // Add to model

    long countTotalTasks = completedTasksCount + ongoingTasksCount + overdueTasksCount;
    model.addAttribute("countTotalTasks", countTotalTasks);

    List<Task> allTasks = taskService.getTasksByUserId(user.getId()); // Fetch completed tasks for the user
    model.addAttribute("allTasks", allTasks); // Add to model

    // Count completed projects related to completed tasks
    long completedProjectCount = taskService.countCompletedProjectsByTasks( allTasks); // Correct method
    System.out.println("CompletedProjects:"+completedProjectCount);

    model.addAttribute("completedProjectCount", completedProjectCount);

    long notCompletedProjects = taskService.countNotCompletedProjectsByTasks( allTasks);
    System.out.println("notCompletedProjects:"+notCompletedProjects);

    model.addAttribute("notCompletedProjects", notCompletedProjects);

    List<Long> completedTasksByMonth = taskService.countCompletedProjectsByMonth(user.getId());
    System.out.println("CompletedProjects:"+completedTasksByMonth);
    model.addAttribute("completedTasksByMonth", completedTasksByMonth); // Add to model

    return "UserReporting"; // Return the view name
}
@GetMapping("/EmpTask")
public String EmployeeTask(Authentication authentication, Model model) {
    User user = getUserFromAuthentication(authentication);


    List<Task> ongoingTasks = taskService.getOngoingTasksByUserId(user.getId());
    model.addAttribute("ongoingTasks", ongoingTasks); // Add ongoing tasks to the mode
    List<Task> completedTasks = taskService.getCompletedTasksByUserId(user.getId());
    model.addAttribute("completedTasks", completedTasks); // Add ongoing tasks to the mode
    
    return "EmpTask";
}

@PostMapping("/updateTask")
public String updateTask( @RequestParam("taskId") Long taskId,
                                    @RequestParam("taskName") String taskName,
                                @RequestParam("employee-select") List<String> employeeIds,
                                @RequestParam("Status") String status,
                                @RequestParam("projectId") String projectId,
                                @RequestParam(value = "dueDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
                                RedirectAttributes redirectAttributes) {

    // Find the task by taskId
    Task task = taskService.getTaskById(taskId);
    if(dueDate==null){
        task.setTaskName(taskName);
        task.setStatus(status);
    
        // Set the project for the task
        Projects project = projectService.findProject(projectId);
        task.setProject(project);
    
        // Fetch and assign users (employees) to the task
        List<User> users = new ArrayList<>();
        for (String empId : employeeIds) {
            User user = userService.findByEmpID(empId);
            users.add(user);
        }
        task.setUser(users); // Set the list of users to the task
    
        // Save the task
        taskService.saveTask(task); // Ensure saveTask method is implemented correctly
    }
    else{
        task.setTaskName(taskName);
        task.setDueDate(dueDate);
        task.setStatus(status);
    
        // Set the project for the task
        Projects project = projectService.findProject(projectId);
        task.setProject(project);
    
        // Fetch and assign users (employees) to the task
        List<User> users = new ArrayList<>();
        for (String empId : employeeIds) {
            User user = userService.findByEmpID(empId);
            users.add(user);
        }
        task.setUser(users); 
    
        // Save the task
        taskService.saveTask(task); // Ensure saveTask method is implemented correctly
    }
    redirectAttributes.addFlashAttribute("successMessage", "Task Updated successfully!");
    return "redirect:/AdminTask";
    }
   @DeleteMapping("/deleteTask/{taskId}")
public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
    taskService.deleteTasksById(taskId); // Implement the delete logic in your service
    return ResponseEntity.noContent().build(); // Return HTTP 204 No Content
}

}
