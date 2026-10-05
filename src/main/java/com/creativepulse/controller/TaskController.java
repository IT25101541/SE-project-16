package com.creativepulse.controller;
import com.creativepulse.model.Task; import com.creativepulse.service.TaskService; import jakarta.validation.Valid; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/tasks") public class TaskController {
 private final TaskService service; public TaskController(TaskService service){this.service=service;}
 @GetMapping public String list(Model m){m.addAttribute("tasks",service.getAllTasks());return "tasks";}
 @GetMapping("/new") public String form(Model m){m.addAttribute("task",new Task());return "task-form";}
 @PostMapping("/save") public String save(@Valid @ModelAttribute("task") Task t,BindingResult r){if(r.hasErrors())return "task-form";service.saveTask(t);return "redirect:/tasks";}
 @GetMapping("/view/{id}") public String view(@PathVariable int id,Model m){Task t=service.getTaskById(id);if(t==null)return "redirect:/tasks";m.addAttribute("task",t);return "task-view";}
 @GetMapping("/edit/{id}") public String edit(@PathVariable int id,Model m){Task t=service.getTaskById(id);if(t==null)return "redirect:/tasks";m.addAttribute("task",t);return "task-form";}
 @PostMapping("/delete/{id}") public String delete(@PathVariable int id){service.deleteTask(id);return "redirect:/tasks";}
 @GetMapping("/search") public String search(@RequestParam(defaultValue="") String keyword,Model m){m.addAttribute("tasks",service.searchTasks(keyword));m.addAttribute("keyword",keyword);return "tasks";}
}
