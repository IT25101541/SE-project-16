package com.creativepulse.controller;

import com.creativepulse.entity.Campaign;
import com.creativepulse.entity.CampaignStatus;
import com.creativepulse.repository.ClientRepository;
import com.creativepulse.repository.EmployeeRepository;
import com.creativepulse.service.CampaignService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    public CampaignController(
            CampaignService campaignService,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository) {

        this.campaignService = campaignService;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    // ============================================================
    // CAMPAIGN DASHBOARD
    // ============================================================

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute(
                "campaigns",
                campaignService.findActive()
        );

        model.addAttribute(
                "total",
                campaignService.total()
        );

        model.addAttribute(
                "active",
                campaignService.active()
        );

        model.addAttribute(
                "completed",
                campaignService.completed()
        );

        return "campaign/dashboard";
    }

    // ============================================================
    // CREATE CAMPAIGN
    // ============================================================

    @GetMapping("/new")
    public String newForm(Model model) {

        model.addAttribute(
                "campaign",
                new Campaign()
        );

        addLookups(model);

        return "campaign/form";
    }

    // ============================================================
    // SAVE CAMPAIGN
    // ============================================================

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("campaign") Campaign campaign,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {

            addLookups(model);

            return "campaign/form";
        }

        try {

            campaignService.save(campaign);

            redirect.addFlashAttribute(
                    "success",
                    "Campaign saved successfully."
            );

            return "redirect:/campaigns";

        } catch (IllegalArgumentException ex) {

            result.reject(
                    "campaign.date",
                    ex.getMessage()
            );

            addLookups(model);

            return "campaign/form";
        }
    }

    // ============================================================
    // EDIT CAMPAIGN
    // ============================================================

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable("id") Long id,
            Model model) {

        model.addAttribute(
                "campaign",
                campaignService.findById(id)
        );

        addLookups(model);

        return "campaign/form";
    }

    // ============================================================
    // VIEW CAMPAIGN
    // ============================================================

    @GetMapping("/view/{id}")
    public String view(
            @PathVariable("id") Long id,
            Model model) {

        model.addAttribute(
                "campaign",
                campaignService.findById(id)
        );

        return "campaign/view";
    }

    // ============================================================
    // CANCEL
    // ============================================================

    @PostMapping("/cancel/{id}")
    public String cancel(
            @PathVariable("id") Long id,
            RedirectAttributes redirect) {

        campaignService.cancel(id);

        redirect.addFlashAttribute(
                "success",
                "Campaign cancelled."
        );

        return "redirect:/campaigns";
    }

    // ============================================================
    // ARCHIVE
    // ============================================================

    @PostMapping("/archive/{id}")
    public String archive(
            @PathVariable("id") Long id,
            RedirectAttributes redirect) {

        campaignService.archive(id);

        redirect.addFlashAttribute(
                "success",
                "Campaign archived."
        );

        return "redirect:/campaigns";
    }

    // ============================================================
    // DELETE
    // ============================================================

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirect) {

        campaignService.delete(id);

        redirect.addFlashAttribute(
                "success",
                "Campaign removed."
        );

        return "redirect:/campaigns";
    }

    // ============================================================
    // LOOKUP DATA
    // ============================================================

    private void addLookups(Model model) {

        model.addAttribute(
                "clients",
                clientRepository.findAll()
        );

        model.addAttribute(
                "employees",
                employeeRepository.findAll()
        );

        model.addAttribute(
                "statuses",
                CampaignStatus.values()
        );
    }
}