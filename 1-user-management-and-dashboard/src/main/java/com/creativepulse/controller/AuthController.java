package com.creativepulse.controller;

import com.creativepulse.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final ClientService clientService;
    private final CampaignService campaignService;
    private final TaskService taskService;
    private final AdvertisementService adService;
    private final InvoiceService invoiceService;
    private final ReportService reportService;

    public AuthController(UserService userService, ClientService clientService,
                          CampaignService campaignService, TaskService taskService,
                          AdvertisementService adService, InvoiceService invoiceService,
                          ReportService reportService) {
        this.userService = userService;
        this.clientService = clientService;
        this.campaignService = campaignService;
        this.taskService = taskService;
        this.adService = adService;
        this.invoiceService = invoiceService;
        this.reportService = reportService;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/access-denied")
    public String accessDenied() { return "access-denied"; }

    /** Public landing page (front page). */
    @GetMapping("/")
    public String home() { return "home"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("clientCount", clientService.count());
        model.addAttribute("campaignCount", campaignService.count());
        model.addAttribute("activeCampaigns", campaignService.countActive());
        model.addAttribute("taskCount", taskService.count());
        model.addAttribute("pendingTasks", taskService.countPending());
        model.addAttribute("adCount", adService.count());
        model.addAttribute("pendingAds", adService.countPendingApproval());
        model.addAttribute("invoiceCount", invoiceService.count());
        model.addAttribute("unpaidInvoices", invoiceService.countUnpaid());
        model.addAttribute("reportCount", reportService.count());
        model.addAttribute("recentCampaigns", campaignService.findAll());
        return "dashboard";
    }
}
