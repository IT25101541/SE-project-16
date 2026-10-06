package com.creativepulse.controller;

import com.creativepulse.model.Client;
import com.creativepulse.repository.CampaignRepository;
import com.creativepulse.repository.InvoiceRepository;
import com.creativepulse.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Client Management (Sales Executive) - full CRUD plus a dedicated details view. */
@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;
    private final CampaignRepository campaignRepository;
    private final InvoiceRepository invoiceRepository;

    public ClientController(ClientService service,
                            CampaignRepository campaignRepository,
                            InvoiceRepository invoiceRepository) {
        this.service = service;
        this.campaignRepository = campaignRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("clients", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "clients/list";
    }

    /** Dedicated Read/View operation for CRUD demonstrations. */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Client client = service.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("campaigns", campaignRepository.findByClientId(id));
        model.addAttribute("invoices", invoiceRepository.findByClientId(id));
        return "clients/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("edit", false);
        return "clients/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("client") Client client,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (client.getEmail() != null && service.emailTaken(client.getEmail(), null)) {
            result.rejectValue("email", "duplicate", "A client with this email already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            return "clients/form";
        }
        service.save(client);
        ra.addFlashAttribute("success", "Client \"" + client.getCompanyName() + "\" added successfully.");
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", service.findById(id));
        model.addAttribute("edit", true);
        return "clients/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("client") Client client,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (client.getEmail() != null && service.emailTaken(client.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "A client with this email already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            return "clients/form";
        }
        service.update(id, client);
        ra.addFlashAttribute("success", "Client updated successfully.");
        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Client deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    "This client cannot be deleted because campaigns or invoices are linked to it.");
        }
        return "redirect:/clients";
    }
}
