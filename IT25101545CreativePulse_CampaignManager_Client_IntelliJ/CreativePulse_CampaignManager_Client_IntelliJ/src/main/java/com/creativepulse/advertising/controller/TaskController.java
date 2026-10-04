package com.creativepulse.advertising.controller;

import com.creativepulse.advertising.dao.TaskDao;
import com.creativepulse.advertising.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/campaign-manager/tasks")
public class TaskController {
    private final TaskDao dao;
    public TaskController(TaskDao dao){this.dao=dao;}
    @GetMapping
    public String list(Model model){
        model.addAttribute("tasks",dao.findAll());
        model.addAttribute("total",dao.count());
        model.addAttribute("pending",dao.pendingCount());
        return "tasks";
    }
    @GetMapping("/new")
    public String form(Model model){
        Task t=new Task(); t.setStatus("Pending"); t.setPriority("Medium");
        model.addAttribute("task",t); return "task-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute Task t){
        if(t.getId()==null)dao.save(t);else dao.update(t);
        return "redirect:/campaign-manager/tasks";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        Task t=dao.findAll().stream().filter(x->x.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("task",t); return "task-form";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){dao.delete(id);return "redirect:/campaign-manager/tasks";}
}
