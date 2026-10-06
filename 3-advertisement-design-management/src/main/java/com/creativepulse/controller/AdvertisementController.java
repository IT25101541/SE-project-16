package com.creativepulse.controller;

import com.creativepulse.model.Advertisement;
import com.creativepulse.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

/** MAJOR FUNCTION 4 - Advertisement Management (Graphic Designer). */
@Controller
@RequestMapping("/advertisements")
public class AdvertisementController {

    private static final List<String> ALLOWED =
            List.of("jpg", "jpeg", "png", "gif", "pdf", "mp4", "psd", "ai");
    private static final long MAX_BYTES = 10L * 1024 * 1024;   // 10 MB

    private final AdvertisementService service;
    private final CampaignService campaignService;
    private final UserService userService;
    private final FileStorageService storage;

    public AdvertisementController(AdvertisementService service, CampaignService campaignService,
                                   UserService userService, FileStorageService storage) {
        this.service = service;
        this.campaignService = campaignService;
        this.userService = userService;
        this.storage = storage;
    }

    private void loadDropdowns(Model model) {
        model.addAttribute("campaigns", campaignService.findAll());
        model.addAttribute("types", Advertisement.AdType.values());
        model.addAttribute("statuses", Advertisement.Status.values());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("ads", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "advertisements/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("ad", service.findById(id));
        return "advertisements/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ad", new Advertisement());
        model.addAttribute("edit", false);
        loadDropdowns(model);
        return "advertisements/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("ad") Advertisement ad, BindingResult result,
                         @RequestParam("designFile") MultipartFile designFile,
                         Principal principal, Model model, RedirectAttributes ra) {

        if (designFile == null || designFile.isEmpty()) {
            result.reject("file.required", "Please choose a design file to upload");
        } else {
            validateFile(designFile, result);
        }

        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            loadDropdowns(model);
            return "advertisements/form";
        }

        ad.setFileName(designFile.getOriginalFilename());
        ad.setStoredFileName(storage.store(designFile));
        ad.setVersion(1);
        userService.findAll().stream()
                .filter(u -> u.getUsername().equals(principal.getName()))
                .findFirst().ifPresent(ad::setUploadedBy);

        service.save(ad);
        ra.addFlashAttribute("success", "Advertisement uploaded successfully.");
        return "redirect:/advertisements";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("ad", service.findById(id));
        model.addAttribute("edit", true);
        loadDropdowns(model);
        return "advertisements/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("ad") Advertisement ad,
                         BindingResult result,
                         @RequestParam("designFile") MultipartFile designFile,
                         Model model, RedirectAttributes ra) {

        if (designFile != null && !designFile.isEmpty()) {
            validateFile(designFile, result);
        }
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            loadDropdowns(model);
            return "advertisements/form";
        }
        if (designFile != null && !designFile.isEmpty()) {
            ad.setFileName(designFile.getOriginalFilename());
            ad.setStoredFileName(storage.store(designFile));
        } else {
            ad.setStoredFileName(null);   // keep the existing file
        }
        service.update(id, ad);
        ra.addFlashAttribute("success", "Advertisement updated successfully.");
        return "redirect:/advertisements";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Advertisement deleted successfully.");
        return "redirect:/advertisements";
    }

    /** STATE pattern: submit a DRAFT/REJECTED design for approval. */
    @PostMapping("/{id}/submit")
    public String submit(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.submitForApproval(id);
            ra.addFlashAttribute("success", "Advertisement submitted for approval.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/advertisements/view/" + id;
    }

    /** STATE pattern: approve a SUBMITTED design. */
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.approve(id);
            ra.addFlashAttribute("success", "Advertisement approved.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/advertisements/view/" + id;
    }

    /** STATE pattern: reject a SUBMITTED design. */
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.reject(id);
            ra.addFlashAttribute("success", "Advertisement rejected.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/advertisements/view/" + id;
    }

    /** STATE pattern: create a new design version. */
    @PostMapping("/{id}/new-version")
    public String newVersion(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.uploadNewVersion(id);
            ra.addFlashAttribute("success", "New design version created.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/advertisements/view/" + id;
    }

    /** File type and size validation with clear error messages. */
    private void validateFile(MultipartFile file, BindingResult result) {
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        int dot = name.lastIndexOf('.');
        String ext = dot == -1 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
        if (!ALLOWED.contains(ext)) {
            result.reject("file.type",
                    "Only these file types are allowed: " + String.join(", ", ALLOWED));
        }
        if (file.getSize() > MAX_BYTES) {
            result.reject("file.size", "File is too large. Maximum allowed size is 10 MB.");
        }
    }
}
