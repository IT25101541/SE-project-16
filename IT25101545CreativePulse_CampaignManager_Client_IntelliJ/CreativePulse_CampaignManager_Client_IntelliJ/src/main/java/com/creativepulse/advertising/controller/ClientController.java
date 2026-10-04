package com.creativepulse.advertising.controller;

import com.creativepulse.advertising.dao.ClientDao;
import com.creativepulse.advertising.model.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientDao dao;
    public ClientController(ClientDao dao){this.dao=dao;}
    @GetMapping
    public String list(Model model){
        model.addAttribute("clients",dao.findAll());
        model.addAttribute("total",dao.count());
        return "clients";
    }
    @GetMapping("/new")
    public String form(Model model){
        Client c=new Client(); c.setStatus("Active");
        model.addAttribute("client",c); return "client-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute Client c){
        if(c.getId()==null)dao.save(c);else dao.update(c);
        return "redirect:/clients";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        model.addAttribute("client",dao.findById(id)); return "client-form";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){dao.delete(id);return "redirect:/clients";}
}
