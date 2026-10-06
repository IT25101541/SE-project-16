package com.creativepulse.controller;

import com.creativepulse.model.Role;
import com.creativepulse.model.User;
import com.creativepulse.service.UserService;
import com.creativepulse.service.TaskService;
import com.creativepulse.service.CampaignService;
import com.creativepulse.service.AdvertisementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MAJOR FUNCTION 1 - User Management (System Administrator).
 * Full CRUD + search + validation.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final TaskService taskService;
    private final CampaignService campaignService;
    private final AdvertisementService advertisementService;

    public UserController(UserService service, TaskService taskService,
                          CampaignService campaignService, AdvertisementService advertisementService) {
        this.service = service;
        this.taskService = taskService;
        this.campaignService = campaignService;
        this.advertisementService = advertisementService;
    }

    @ModelAttribute("roles")
    public Role[] roles() { return Role.values(); }

    /** READ - list + search */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("users", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "users/list";
    }

    /** READ - dedicated user details page */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        User user = service.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("tasks", taskService.findAll().stream()
                .filter(t -> t.getAssignee() != null && id.equals(t.getAssignee().getId())).toList());
        model.addAttribute("campaigns", campaignService.findAll().stream()
                .filter(c -> c.getManager() != null && id.equals(c.getManager().getId())).toList());
        model.addAttribute("advertisements", advertisementService.findAll().stream()
                .filter(a -> a.getUploadedBy() != null && id.equals(a.getUploadedBy().getId())).toList());
        return "users/view";
    }

    /** CREATE - show blank form */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("edit", false);
        return "users/form";
    }

    /** CREATE - save */
    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult result,
                         @RequestParam(required = false) String rawPassword,
                         @RequestParam(required = false) String confirmPassword,
                         Model model, RedirectAttributes ra) {

        validatePassword(rawPassword, confirmPassword, result, true);

        if (service.usernameTaken(user.getUsername(), null)) {
            result.rejectValue("username", "duplicate", "This username is already taken");
        }
        if (service.emailTaken(user.getEmail(), null)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            return "users/form";
        }

        service.create(user, rawPassword);
        ra.addFlashAttribute("success", "User \"" + user.getUsername() + "\" created successfully.");
        return "redirect:/users";
    }

    /** UPDATE - show filled form */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findById(id));
        model.addAttribute("edit", true);
        return "users/form";
    }

    /** UPDATE - save */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("user") User user,
                         BindingResult result,
                         @RequestParam(required = false) String rawPassword,
                         @RequestParam(required = false) String confirmPassword,
                         Model model, RedirectAttributes ra) {

        validatePassword(rawPassword, confirmPassword, result, false);

        if (service.usernameTaken(user.getUsername(), id)) {
            result.rejectValue("username", "duplicate", "This username is already taken");
        }
        if (service.emailTaken(user.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            return "users/form";
        }

        service.update(id, user, rawPassword);
        ra.addFlashAttribute("success", "User updated successfully.");
        return "redirect:/users";
    }

    /** DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        User user = service.findById(id);
        if ("admin".equals(user.getUsername())) {
            ra.addFlashAttribute("error", "The main admin account cannot be deleted.");
            return "redirect:/users";
        }
        service.delete(id);
        ra.addFlashAttribute("success", "User deleted successfully.");
        return "redirect:/users";
    }

    /** Business validation rules for the password fields. */
    private void validatePassword(String raw, String confirm, BindingResult result, boolean required) {
        if (required && (raw == null || raw.isBlank())) {
            result.reject("password.required", "Password is required");
            return;
        }
        if (raw != null && !raw.isBlank()) {
            if (raw.length() < 4) {
                result.reject("password.short", "Password must be at least 4 characters long");
            }
            if (!raw.equals(confirm)) {
                result.reject("password.mismatch", "Password and Confirm Password do not match");
            }
        }
    }
}
