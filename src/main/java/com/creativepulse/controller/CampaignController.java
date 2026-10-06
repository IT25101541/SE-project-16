package com.creativepulse.controller;

import com.creativepulse.model.Campaign;
import com.creativepulse.model.Role;
import com.creativepulse.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** MAJOR FUNCTION 3 - Campaign Management (Campaign Manager). */
@Controller
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService service;
    private final ClientService clientService;
    private final UserService userService;

    public CampaignController(CampaignService service, ClientService clientService, UserService userService) {
        this.service = service;
        this.clientService = clientService;
        this.userService = userService;
    }

    private void loadDropdowns(Model model) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("managers", userService.findByRole(Role.MANAGER));
        model.addAttribute("statuses", Campaign.Status.values());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("campaigns", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "campaigns/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("campaign", new Campaign());
        model.addAttribute("edit", false);
        loadDropdowns(model);
        return "campaigns/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("campaign") Campaign campaign,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validateDates(campaign, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            loadDropdowns(model);
            return "campaigns/form";
        }
        service.save(campaign);
        ra.addFlashAttribute("success", "Campaign \"" + campaign.getName() + "\" created successfully.");
        return "redirect:/campaigns";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("campaign", service.findById(id));
        model.addAttribute("edit", true);
        loadDropdowns(model);
        return "campaigns/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("campaign") Campaign campaign,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validateDates(campaign, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            loadDropdowns(model);
            return "campaigns/form";
        }
        service.update(id, campaign);
        ra.addFlashAttribute("success", "Campaign updated successfully.");
        return "redirect:/campaigns";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("campaign", service.findById(id));
        return "campaigns/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Campaign deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    "This campaign cannot be deleted because tasks, advertisements or invoices are linked to it.");
        }
        return "redirect:/campaigns";
    }

    /** Cross-field business rule: end date must be after start date. */
    private void validateDates(Campaign c, BindingResult result) {
        if (c.getStartDate() != null && c.getEndDate() != null
                && !c.getEndDate().isAfter(c.getStartDate())) {
            result.rejectValue("endDate", "date.order", "End date must be after the start date");
        }
        if (c.getStatus() == Campaign.Status.COMPLETED && c.getProgress() < 100) {
            result.rejectValue("progress", "progress.completed",
                    "A completed campaign must have 100% progress");
        }
    }
}
