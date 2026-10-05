package com.creativepulse.service;

import com.creativepulse.dao.TaskDAO;
import com.creativepulse.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskDAO dao;

    public TaskService(TaskDAO dao) {
        this.dao = dao;
    }

    public List<Task> getAllTasks() {
        return dao.findAll();
    }

    public Task getTaskById(int id) {
        return dao.findById(id);
    }

    public void saveTask(Task t) {
        if (t.getTaskId() == 0) {
            dao.save(t);
        } else {
            dao.update(t);
        }
    }

    public void deleteTask(int id) {
        dao.delete(id);
    }

    public List<Task> searchTasks(String keyword) {
        return keyword == null || keyword.isBlank() ? dao.findAll() : dao.search(keyword);
    }
}