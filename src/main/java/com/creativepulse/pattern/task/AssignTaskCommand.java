package com.creativepulse.pattern.task;

import com.creativepulse.model.TaskItem;
import com.creativepulse.model.User;
import com.creativepulse.service.TaskService;

/**
 * Concrete Command – Assign (or re-assign) a task to an employee.
 * Compatible with creativepulse 10-3 (TaskService has save()).
 */
public class AssignTaskCommand implements TaskCommand {

    private final TaskItem task;
    private final User assignee;
    private final TaskService taskService;

    public AssignTaskCommand(TaskItem task, User assignee, TaskService taskService) {
        this.task = task;
        this.assignee = assignee;
        this.taskService = taskService;
    }

    @Override
    public void execute() {
        task.setAssignee(assignee);
        task.setStatus(TaskItem.Status.TODO);
        taskService.save(task);
        // Future: trigger notification to the assignee
    }

    @Override
    public String description() {
        return "Assign task \"" + task.getTitle() + "\" to " + assignee.getFullName();
    }
}
