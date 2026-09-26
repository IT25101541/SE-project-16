package com.creativepulse.controller;

import com.creativepulse.model.Task;
import com.creativepulse.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Display all tasks
    @GetMapping("/tasks")
    public String showTasks(Model model) {

        model.addAttribute("tasks", taskService.getAllTasks());

        return "tasks";
    }

    // Show add task form
    @GetMapping("/tasks/add")
    public String showAddTaskForm(Model model) {

        model.addAttribute("task", new Task());

        return "task-form";
    }

    // Save new task
    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task) {

        taskService.addTask(task);

        return "redirect:/tasks";
    }

    // Show edit task form
    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(
            @PathVariable("id") int id,
            Model model) {

        model.addAttribute("task", taskService.getTaskById(id));

        return "task-form";
    }

    // Update task
    @PostMapping("/tasks/update")
    public String updateTask(@ModelAttribute Task task) {

        taskService.updateTask(task);

        return "redirect:/tasks";
    }

    // Delete task
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable("id") int id) {

        taskService.deleteTask(id);

        return "redirect:/tasks";
    }
}