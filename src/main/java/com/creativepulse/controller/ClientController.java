package com.creativepulse.controller;

import com.creativepulse.entity.Client;
import com.creativepulse.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;
    public ClientController(ClientService service) { this.service = service; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", service.findAll());
        return "client/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("client", new Client());
        return "client/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("client") Client client, BindingResult result) {
        if (result.hasErrors()) return "client/form";
        service.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("client", service.findById(id));
        return "client/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/clients";
    }
}
