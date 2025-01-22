package com.example.taskmanagement.demo.service.Impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar; // Import Calendar
import java.util.Collections;// Im port Collections

import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.demo.entity.Task;
import com.example.taskmanagement.demo.repository.ProjectRepository;
import com.example.taskmanagement.demo.repository.TaskRepository;
import com.example.taskmanagement.demo.service.TaskService;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    // Method to count completed tasks
    public long countCompletedTasks() {
        return taskRepository.countByStatus("completed"); // Use the appropriate status
    }
    public long countOngoingTasks() {
        return taskRepository.countByStatus("ongoing"); // Use the appropriate status
    }
    public long countOverDueTask() {
        return taskRepository.countByStatus("overdue"); // Use the appropriate status
    }
    public long countTotalTasks() {
        return taskRepository.count(); // Use the appropriate status
    }
     @Override
     public List<Task> getCompletedTasksByUserId(Long userId) {
        return taskRepository.findByUser_IdAndStatus(userId, "COMPLETED"); // Fetch only completed tasks
    }
    @Override
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserIdAndStatusIn(userId, List.of("completed","ongoing", "overdue")); // Fetch only completed tasks
    }
    @Override
    public List<Task> getOngoingOnlyTasksByUserId(Long userId) {
        // Fetch ongoing and overdue tasks
        return taskRepository.findByUser_IdAndStatusIn(userId, List.of("ongoing"));
    }
    @Override
    public List<Task> getOverdueTasksByUserId(Long userId) {
        // Fetch ongoing and overdue tasks
        return taskRepository.findByUser_IdAndStatusIn(userId, List.of("overdue"));
    }
    @Override
    public List<Task> getOngoingTasksByUserId(Long userId) {
        // Fetch ongoing and overdue tasks
        return taskRepository.findByUser_IdAndStatusIn(userId, List.of("ongoing", "overdue"));
    }
    @Override
     public Long CountOngoingTasksByUserId(Long userId) {
        return taskRepository.countByUser_IdAndStatus(userId, "ongoing"); // Fetch only completed tasks
    }
    @Override
     public Long CountCompletedTasksByUserId(Long userId) {
        return taskRepository.countByUser_IdAndStatus(userId, "COMPLETED"); // Fetch only completed tasks
    }
    @Override
    public Long CountOverdueTasksByUserId(Long userId) {
       return taskRepository.countByUser_IdAndStatus(userId, "overdue"); // Fetch only completed tasks
   }
    

    
    @Override
    public List<Task> getcompleteTasks() {
        return taskRepository.findByStatus("completed");
    }
    @Override
    public List<Task> getOngoingTasks() {
        return taskRepository.findByStatus("ongoing");
    }
    @Override
    public List<Task> getOverDueTasks() {
        return taskRepository.findByStatus("Overdue");
    }
    @Override
    public Task saveTask(Task task){
        return taskRepository.save(task);
    }
    @Override
    public List<Long> countCompletedProjectsByMonth() {
    // Assuming you have a method to fetch all completed tasks
    List<Task> completedTasks = taskRepository.findByStatus("completed");
    
    // Create a list to hold the count of projects for each month
    List<Long> monthlyCounts = new ArrayList<>(Collections.nCopies(12, 0L)); // Initialize to 0 for each month

    for (Task task : completedTasks) {
        // Assuming task has a dueDate that indicates when it was completed
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getDueDate());
        int month = cal.get(Calendar.MONTH); // 0-11 for Jan-Dec
        monthlyCounts.set(month, monthlyCounts.get(month) + 1); // Increment the count for that month
    }

    return monthlyCounts;
}
@Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }
    @Override
    public long countCompletedProjectsByTasks(List<Task> completedTasks) {
        Set<String> projectIds = new HashSet<>();

        for (Task task : completedTasks) {
            if (task.getProject() != null) {
                projectIds.add(task.getProject().getProjectId()); // Collect unique project IDs
            }
        }

        // Count completed projects based on the collected project IDs
        return projectRepository.countByProjectIdInAndStatus(projectIds, "COMPLETED"); // Assuming "COMPLETED" is the status for completed projects
    }
    @Override
    public long countNotCompletedProjectsByTasks(List<Task> completedTasks) {
        Set<String> projectIds = new HashSet<>();

        for (Task task : completedTasks) {
            if (task.getProject() != null) {
                projectIds.add(task.getProject().getProjectId()); // Collect unique project IDs
            }
        }

        // Count completed projects based on the collected project IDs
        return projectRepository.countByProjectIdInAndStatusIn(projectIds, List.of("ongoing", "overdue")); // Assuming "COMPLETED" is the status for completed projects
    }

    @Override
    public List<Long> countCompletedProjectsByMonth(Long userId) {
    // Assuming you have a method to fetch all completed tasks
    List<Task> completedTasks = taskRepository.findByUser_IdAndStatus(userId,"completed");
    
    // Create a list to hold the count of projects for each month
    List<Long> monthlyCounts = new ArrayList<>(Collections.nCopies(12, 0L)); // Initialize to 0 for each month

    for (Task task : completedTasks) {
        // Assuming task has a dueDate that indicates when it was completed
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getDueDate());
        int month = cal.get(Calendar.MONTH); // 0-11 for Jan-Dec
        monthlyCounts.set(month, monthlyCounts.get(month) + 1); // Increment the count for that month
    }

    return monthlyCounts;
}
        @Override
        public List<Task> findTasksByProjectId(String projectId) {
            return taskRepository.findByProject_ProjectId(projectId); // This assumes you have a method in the repository to fetch tasks by project ID
        }
        
        @Override
        public List<Task> getAlltasks() {
            return taskRepository.findAll(); // This assumes you have a method in the repository to fetch tasks by project ID
        }
        @Override
        public List<Task> findTasksByStatus() {
            return taskRepository.findByStatusIn(List.of("ongoing", "overdue")); // This assumes you have a method in the repository to fetch tasks by project ID
        }
        @Override
        public void deleteTasksById(Long taskId) {
            taskRepository.deleteById(taskId);
            }
            

}
