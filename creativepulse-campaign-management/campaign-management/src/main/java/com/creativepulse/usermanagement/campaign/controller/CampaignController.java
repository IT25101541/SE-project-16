package com.creativepulse.usermanagement.campaign.controller;

import com.creativepulse.usermanagement.campaign.dto.CampaignForm;
import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignCategory;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import com.creativepulse.usermanagement.campaign.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * Campaign management.
 * <ul>
 *   <li>View (list + details): Admin, Campaign Manager, Sales, Designer, Employee, Finance</li>
 *   <li>Create, edit, remove: Admin and Campaign Manager only</li>
 * </ul>
 * Clients have no access to this module.
 */
@Controller
@RequestMapping("/campaigns")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES','DESIGNER','EMPLOYEE','FINANCE')")
public class CampaignController {

    private static final String CAN_EDIT = "hasAnyRole('ADMIN','MANAGER')";

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    // -------------------------------------------------------------------- view

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String query,
                       @RequestParam(value = "status", required = false) CampaignStatus status,
                       Model model) {
        model.addAttribute("campaigns", campaignService.search(query, status));
        model.addAttribute("counts", campaignService.countByStatus());
        model.addAttribute("statuses", CampaignStatus.values());
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("activePage", "campaigns");
        return "campaigns/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("campaign", campaignService.get(id));
        model.addAttribute("activePage", "campaigns");
        return "campaigns/detail";
    }

    // ------------------------------------------------------------------ create

    @GetMapping("/new")
    @PreAuthorize(CAN_EDIT)
    public String newForm(Model model) {
        model.addAttribute("form", new CampaignForm());
        addFormData(model, "create", null);
        return "campaigns/form";
    }

    @PostMapping("/new")
    @PreAuthorize(CAN_EDIT)
    public String create(@Valid @ModelAttribute("form") CampaignForm form,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model,
                         RedirectAttributes redirect) {
        checkDates(form, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                Campaign created = campaignService.create(form, authentication.getName());
                redirect.addFlashAttribute("success", "Campaign created");
                return "redirect:/campaigns/" + created.getId();
            } catch (IllegalArgumentException e) {
                bindingResult.reject("campaign.invalid", e.getMessage());
            }
        }
        addFormData(model, "create", null);
        return "campaigns/form";
    }

    // -------------------------------------------------------------------- edit

    @GetMapping("/{id}/edit")
    @PreAuthorize(CAN_EDIT)
    public String editForm(@PathVariable Long id, Model model) {
        Campaign campaign = campaignService.get(id);
        model.addAttribute("form", CampaignForm.from(campaign));
        addFormData(model, "edit", campaign);
        return "campaigns/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize(CAN_EDIT)
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") CampaignForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        Campaign campaign = campaignService.get(id);
        checkDates(form, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                campaignService.update(id, form);
                redirect.addFlashAttribute("success", "Campaign updated");
                return "redirect:/campaigns/" + id;
            } catch (IllegalArgumentException e) {
                bindingResult.reject("campaign.invalid", e.getMessage());
            }
        }
        addFormData(model, "edit", campaign);
        return "campaigns/form";
    }

    // ------------------------------------------------------------------ delete

    @PostMapping("/{id}/delete")
    @PreAuthorize(CAN_EDIT)
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            campaignService.delete(id);
            redirect.addFlashAttribute("success", "Campaign removed");
            return "redirect:/campaigns";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/campaigns/" + id;
        }
    }

    // ----------------------------------------------------------------- helpers

    private void checkDates(CampaignForm form, BindingResult bindingResult) {
        if (form.getStartDate() != null && form.getEndDate() != null
                && form.getEndDate().isBefore(form.getStartDate())) {
            bindingResult.rejectValue("endDate", "range", "End date cannot be before the start date");
        }
    }

    private void addFormData(Model model, String mode, Campaign campaign) {
        model.addAttribute("mode", mode);
        model.addAttribute("campaign", campaign);
        model.addAttribute("categories", CampaignCategory.values());
        model.addAttribute("statuses", CampaignStatus.values());
        model.addAttribute("members", campaignService.assignableMembers());
        model.addAttribute("activePage", "campaigns");
    }
}
