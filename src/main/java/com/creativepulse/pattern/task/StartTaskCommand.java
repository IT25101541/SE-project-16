package com.creativepulse.pattern.task;

import com.creativepulse.model.TaskItem;
import com.creativepulse.service.TaskService;

/**
 * Concrete Command – Mark a task as IN_PROGRESS.
 * Compatible with creativepulse 10-3.
 */
public class StartTaskCommand implements TaskCommand {

    private final TaskItem task;
    private final TaskService taskService;

    public StartTaskCommand(TaskItem task, TaskService taskService) {
        this.task = task;
        this.taskService = taskService;
    }

    @Override
    public void execute() {
        if (task.getStatus() != TaskItem.Status.TODO) {
            throw new IllegalStateException("Only TODO tasks can be started.");
        }
        task.setStatus(TaskItem.Status.IN_PROGRESS);
        taskService.save(task);
    }

    @Override
    public String description() {
        return "Start task \"" + task.getTitle() + "\"";
    }
}
