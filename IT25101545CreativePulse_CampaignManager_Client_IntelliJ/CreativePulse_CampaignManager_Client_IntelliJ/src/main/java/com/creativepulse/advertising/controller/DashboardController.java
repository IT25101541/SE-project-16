package com.creativepulse.advertising.controller;

import com.creativepulse.advertising.dao.CampaignDao;
import com.creativepulse.advertising.dao.ClientDao;
import com.creativepulse.advertising.dao.TaskDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final CampaignDao campaigns;
    private final TaskDao tasks;
    private final ClientDao clients;
    public DashboardController(CampaignDao campaigns,TaskDao tasks,ClientDao clients){
        this.campaigns=campaigns;this.tasks=tasks;this.clients=clients;
    }
    @GetMapping("/campaign-manager")
    public String dashboard(Model model){
        model.addAttribute("campaignCount",campaigns.count());
        model.addAttribute("activeCampaigns",campaigns.activeCount());
        model.addAttribute("taskCount",tasks.count());
        model.addAttribute("pendingTasks",tasks.pendingCount());
        model.addAttribute("clientCount",clients.count());
        return "campaign-manager-dashboard";
    }
}
