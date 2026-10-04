package com.creativepulse.controller;

import com.creativepulse.dto.AdvertisementDto;
import com.creativepulse.dto.AdvertisementVersionDto;
import com.creativepulse.entity.enums.AdFormat;
import com.creativepulse.entity.enums.ReviewStatus;
import com.creativepulse.service.AdvertisementService;
import com.creativepulse.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final CampaignService campaignService;

    public AdvertisementController(AdvertisementService advertisementService, CampaignService campaignService) {
        this.advertisementService = advertisementService;
        this.campaignService = campaignService;
    }

    // --- List All Ads ---
    @GetMapping
    public String listAdvertisements(Model model) {
        model.addAttribute("advertisements", advertisementService.getAllAdvertisements());
        return "advertisements/list";
    }

    // --- Create Ad for Campaign ---
    @GetMapping("/campaign/{campaignId}/create")
    public String showCreateForm(@PathVariable Long campaignId, Model model) {
        AdvertisementDto dto = new AdvertisementDto();
        dto.setCampaignId(campaignId);
        
        model.addAttribute("advertisementDto", dto);
        model.addAttribute("formats", AdFormat.values());
        model.addAttribute("campaignName", campaignService.getCampaignById(campaignId)
                .map(c -> c.getName()).orElse("Unknown"));
        return "advertisements/create";
    }

    @PostMapping("/campaign/{campaignId}/create")
    public String createAdvertisement(@PathVariable Long campaignId,
                                      @Valid @ModelAttribute("advertisementDto") AdvertisementDto dto,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formats", AdFormat.values());
            model.addAttribute("campaignName", campaignService.getCampaignById(campaignId).map(c -> c.getName()).orElse("Unknown"));
            return "advertisements/create";
        }

        try {
            dto.setCampaignId(campaignId);
            advertisementService.createAdvertisement(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Advertisement created successfully.");
            return "redirect:/campaigns/" + campaignId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "advertisements/create";
        }
    }

    // --- View Ad Details & Versions ---
    @GetMapping("/{id}")
    public String viewAdvertisement(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return advertisementService.getAdvertisementById(id)
                .map(ad -> {
                    model.addAttribute("advertisement", ad);
                    model.addAttribute("versions", advertisementService.getVersionsForAdvertisement(id));
                    model.addAttribute("newVersionDto", new AdvertisementVersionDto());
                    model.addAttribute("reviewStatuses", ReviewStatus.values());
                    return "advertisements/details";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Advertisement not found.");
                    return "redirect:/campaigns";
                });
    }

    // --- Add Version ---
    @PostMapping("/{id}/versions")
    public String addVersion(@PathVariable Long id,
                             @Valid @ModelAttribute("newVersionDto") AdvertisementVersionDto versionDto,
                             BindingResult result,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add version. Asset URL is required.");
            return "redirect:/advertisements/" + id;
        }

        try {
            advertisementService.addVersion(id, versionDto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "New version uploaded successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/advertisements/" + id;
    }

    // --- Review Version ---
    @PostMapping("/versions/{versionId}/review")
    public String reviewVersion(@PathVariable Long versionId,
                                @RequestParam ReviewStatus reviewStatus,
                                @RequestParam(required = false) String feedback,
                                @RequestParam Long advertisementId,
                                RedirectAttributes redirectAttributes) {
        try {
            advertisementService.reviewVersion(versionId, reviewStatus, feedback);
            redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/advertisements/" + advertisementId;
    }
}
