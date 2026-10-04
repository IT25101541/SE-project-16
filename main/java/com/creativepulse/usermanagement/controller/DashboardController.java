package com.creativepulse.usermanagement.controller;

import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Role-based dashboard: each role sees its own module cards; admins also see system statistics. */
@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("modules", user.getRole().getModules());
        model.addAttribute("activePage", "dashboard");

        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("stats", userService.getDashboardStats());
        }
        return "dashboard";
    }
}
