package com.creativepulse.pattern.task;

import com.creativepulse.model.TaskItem;
import com.creativepulse.service.TaskService;

/**
 * Concrete Command – Mark a task as DONE.
 * Compatible with creativepulse 10-3.
 */
public class CompleteTaskCommand implements TaskCommand {

    private final TaskItem task;
    private final TaskService taskService;

    public CompleteTaskCommand(TaskItem task, TaskService taskService) {
        this.task = task;
        this.taskService = taskService;
    }

    @Override
    public void execute() {
        task.setStatus(TaskItem.Status.DONE);
        taskService.save(task);
        // Future: update campaign progress, send notification, etc.
    }

    @Override
    public String description() {
        return "Complete task \"" + task.getTitle() + "\"";
    }
}
