package com.creativepulse.usermanagement.controller;

import com.creativepulse.usermanagement.dto.AdminUserForm;
import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.UserStatus;
import com.creativepulse.usermanagement.service.CreatedUser;
import com.creativepulse.usermanagement.service.DuplicateEmailException;
import com.creativepulse.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Administrator-only user management (the /admin/** path is protected in SecurityConfig). */
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String query, Model model) {
        model.addAttribute("users", userService.search(query));
        model.addAttribute("roles", Role.values());
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("activePage", "users");
        return "admin/users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("form", new AdminUserForm());
        model.addAttribute("roles", Role.values());
        model.addAttribute("activePage", "users");
        return "admin/user-form";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("form") AdminUserForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirect) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("activePage", "users");

        if (bindingResult.hasErrors()) {
            return "admin/user-form";
        }
        try {
            CreatedUser created = userService.createByAdmin(form);
            redirect.addFlashAttribute("createdEmail", created.getUser().getEmail());
            redirect.addFlashAttribute("createdPassword", created.getTemporaryPassword());
            return "redirect:/admin/users";
        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue("email", "duplicate", e.getMessage());
            return "admin/user-form";
        }
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam Role role,
                             Authentication authentication,
                             RedirectAttributes redirect) {
        return run(redirect, "Role updated", () -> userService.changeRole(id, role, authentication.getName()));
    }

    @PostMapping("/{id}/suspend")
    public String suspend(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        return run(redirect, "Account suspended",
                () -> userService.setStatus(id, UserStatus.SUSPENDED, authentication.getName()));
    }

    @PostMapping("/{id}/reactivate")
    public String reactivate(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        return run(redirect, "Account reactivated",
                () -> userService.setStatus(id, UserStatus.ACTIVE, authentication.getName()));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        return run(redirect, "Account deleted", () -> userService.delete(id, authentication.getName()));
    }

    private String run(RedirectAttributes redirect, String successMessage, Runnable action) {
        try {
            action.run();
            redirect.addFlashAttribute("success", successMessage);
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
