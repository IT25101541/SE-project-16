package com.creativepulse.service;

import com.creativepulse.model.TaskItem;
import com.creativepulse.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) { this.repo = repo; }

    public List<TaskItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByTitleContainingIgnoreCase(keyword);
    }

    public List<TaskItem> findAll() { return repo.findAll(); }

    public TaskItem findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id " + id));
    }

    public TaskItem save(TaskItem task) { return repo.save(task); }

    public TaskItem update(Long id, TaskItem form) {
        TaskItem db = findById(id);
        db.setTitle(form.getTitle());
        db.setDescription(form.getDescription());
        db.setCampaign(form.getCampaign());
        db.setAssignee(form.getAssignee());
        db.setDueDate(form.getDueDate());
        db.setStatus(form.getStatus());
        db.setPriority(form.getPriority());
        return repo.save(db);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public long count() { return repo.count(); }
    public long countPending() { return repo.countByStatus(TaskItem.Status.TODO); }
}
