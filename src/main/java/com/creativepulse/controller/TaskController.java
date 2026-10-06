package com.creativepulse.controller;

import com.creativepulse.model.TaskItem;
import com.creativepulse.service.*;
import com.creativepulse.pattern.task.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** MAJOR FUNCTION 2 - Employee Task Management (Campaign Manager). */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final CampaignService campaignService;
    private final UserService userService;
    private final TaskCommandInvoker invoker;

    public TaskController(TaskService service, CampaignService campaignService,
                          UserService userService, TaskCommandInvoker invoker) {
        this.service = service;
        this.campaignService = campaignService;
        this.userService = userService;
        this.invoker = invoker;
    }

    private void loadDropdowns(Model model) {
        // Only campaigns that can still accept a task with a valid due date
        // (i.e. not already ended) should be assignable to a new task.
        model.addAttribute("campaigns", campaignService.findAll().stream()
                .filter(c -> c.getEndDate() == null || !c.getEndDate().isBefore(LocalDate.now()))
                .toList());
        model.addAttribute("employees", userService.findAll());
        model.addAttribute("statuses", TaskItem.Status.values());
        model.addAttribute("priorities", TaskItem.Priority.values());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("tasks", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "tasks/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("task", service.findById(id));
        return "tasks/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("task", new TaskItem());
        model.addAttribute("edit", false);
        loadDropdowns(model);
        return "tasks/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("task") TaskItem task,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validate(task, result, true);
        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            loadDropdowns(model);
            return "tasks/form";
        }
        service.save(task);
        ra.addFlashAttribute("success", "Task assigned successfully.");
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", service.findById(id));
        model.addAttribute("edit", true);
        loadDropdowns(model);
        return "tasks/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("task") TaskItem task,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validate(task, result, false);
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            loadDropdowns(model);
            return "tasks/form";
        }
        service.update(id, task);
        ra.addFlashAttribute("success", "Task updated successfully.");
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Task removed successfully.");
        return "redirect:/tasks";
    }

    /** COMMAND pattern: assign a task through the command invoker. */
    @PostMapping("/{taskId}/assign")
    public String assign(@PathVariable Long taskId, @RequestParam Long userId,
                         RedirectAttributes ra) {
        TaskItem task = service.findById(taskId);
        var assignee = userService.findById(userId);
        invoker.execute(new AssignTaskCommand(task, assignee, service));
        ra.addFlashAttribute("success", "Task assigned successfully.");
        return "redirect:/tasks";
    }

    /** COMMAND pattern: start a TODO task. */
    @PostMapping("/{taskId}/start")
    public String start(@PathVariable Long taskId, RedirectAttributes ra) {
        TaskItem task = service.findById(taskId);
        invoker.execute(new StartTaskCommand(task, service));
        ra.addFlashAttribute("success", "Task started successfully.");
        return "redirect:/tasks";
    }

    /** COMMAND pattern: complete a task. */
    @PostMapping("/{taskId}/complete")
    public String complete(@PathVariable Long taskId, RedirectAttributes ra) {
        TaskItem task = service.findById(taskId);
        invoker.execute(new CompleteTaskCommand(task, service));
        ra.addFlashAttribute("success", "Task completed successfully.");
        return "redirect:/tasks";
    }

    private void validate(TaskItem t, BindingResult result, boolean isNew) {
        if (isNew && t.getDueDate() != null && t.getDueDate().isBefore(LocalDate.now())) {
            result.rejectValue("dueDate", "date.past", "Due date cannot be in the past");
        }
        if (t.getDueDate() != null && t.getCampaign() != null
                && t.getCampaign().getStartDate() != null
                && t.getDueDate().isBefore(t.getCampaign().getStartDate())) {
            result.rejectValue("dueDate", "date.beforeCampaign",
                    "Due date cannot be before the campaign start date ("
                            + t.getCampaign().getStartDate() + ")");
        }
        if (t.getDueDate() != null && t.getCampaign() != null
                && t.getCampaign().getEndDate() != null
                && t.getDueDate().isAfter(t.getCampaign().getEndDate())) {
            result.rejectValue("dueDate", "date.afterCampaign",
                    "Due date cannot be later than the campaign end date ("
                            + t.getCampaign().getEndDate() + ")");
        }
    }
}