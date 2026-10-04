package com.creativepulse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    // "/campaigns" is handled by CampaignController

    @GetMapping("/advertisements")
    public String advertisements() {
        return "advertisements";
    }

    @GetMapping("/billing")
    public String billing() {
        return "billing";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
}
