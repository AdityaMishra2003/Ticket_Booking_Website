package com.example.taskmanagement.demo.service;

import java.util.List;

import com.example.taskmanagement.demo.entity.Task;

public interface TaskService {
    public long countCompletedTasks();
    public List<Task> getCompletedTasksByUserId(Long userId);
    public List<Task> getOngoingTasksByUserId(Long userId);
    public List<Task> getcompleteTasks();
    public Task saveTask(Task task);
    public List<Task> getOngoingTasks();
    public List<Task> getOverDueTasks();
    public long countOngoingTasks();
    public long countOverDueTask();
    public long countTotalTasks();
    public List<Long> countCompletedProjectsByMonth();
    public Task getTaskById(Long taskId);
    public Long CountCompletedTasksByUserId(Long userId);
    public Long CountOngoingTasksByUserId(Long userId);
    public Long CountOverdueTasksByUserId(Long userId);
    public long countCompletedProjectsByTasks(List<Task> completedTasks);
    public long countNotCompletedProjectsByTasks(List<Task> completedTasks);
    public List<Long> countCompletedProjectsByMonth(Long userId);
    public List<Task> getTasksByUserId(Long userId);
    public List<Task> findTasksByProjectId(String projectId);
    public List<Task> getAlltasks();
    public List<Task> getOngoingOnlyTasksByUserId(Long userId);
    public List<Task> getOverdueTasksByUserId(Long userId);
    public List<Task> findTasksByStatus() ;
    public void deleteTasksById(Long taskId);

}
