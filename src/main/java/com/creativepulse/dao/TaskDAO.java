package com.creativepulse.dao;

import com.creativepulse.model.Task;
import java.util.List;

public interface TaskDAO {
    List<Task> findAll();
    Task findById(int taskId);
    void save(Task task);
    void update(Task task);
    void delete(int taskId);
    List<Task> search(String keyword);
}