package com.creativepulse.advertising.controller;

import com.creativepulse.advertising.dao.CampaignDao;
import com.creativepulse.advertising.model.Campaign;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/campaign-manager/campaigns")
public class CampaignController {
    private final CampaignDao dao;
    public CampaignController(CampaignDao dao){this.dao=dao;}

    @GetMapping
    public String list(Model model){
        model.addAttribute("campaigns",dao.findAll());
        model.addAttribute("total",dao.count());
        model.addAttribute("active",dao.activeCount());
        model.addAttribute("completed",dao.completedCount());
        return "campaigns";
    }
    @GetMapping("/new")
    public String form(Model model){
        Campaign c=new Campaign();
        c.setStatus("Planned"); c.setProgress(0);
        model.addAttribute("campaign",c); return "campaign-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute Campaign c){
        if(c.getId()==null) dao.save(c); else dao.update(c);
        return "redirect:/campaign-manager/campaigns";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        model.addAttribute("campaign",dao.findById(id)); return "campaign-form";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        dao.delete(id); return "redirect:/campaign-manager/campaigns";
    }
}
