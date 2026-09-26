package com.creativepulse.service;

import com.creativepulse.dao.TaskDAO;
import com.creativepulse.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }

    public Task getTaskById(int id) {
        return taskDAO.getTaskById(id);
    }

    public void addTask(Task task) {
        taskDAO.addTask(task);
    }

    public void updateTask(Task task) {
        taskDAO.updateTask(task);
    }

    public void deleteTask(int id) {
        taskDAO.deleteTask(id);
    }
}