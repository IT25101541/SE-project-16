package com.creativepulse.controller;

import com.creativepulse.model.Invoice;
import com.creativepulse.model.Payment;
import com.creativepulse.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/** MAJOR FUNCTION 5 - Billing and Payment Management (Finance Officer). */
@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService service;
    private final PaymentService paymentService;
    private final ClientService clientService;
    private final CampaignService campaignService;

    public InvoiceController(InvoiceService service, PaymentService paymentService,
                             ClientService clientService, CampaignService campaignService) {
        this.service = service;
        this.paymentService = paymentService;
        this.clientService = clientService;
        this.campaignService = campaignService;
    }

    private void loadDropdowns(Model model) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("campaigns", campaignService.findAll());
        model.addAttribute("statuses", Invoice.Status.values());
        model.addAttribute("methods", Payment.Method.values());
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("invoices", service.search(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("invoiceService", service);
        return "invoices/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("edit", false);
        loadDropdowns(model);
        return "invoices/form";
    }

    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("invoice") Invoice invoice,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validate(invoice, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            loadDropdowns(model);
            return "invoices/form";
        }
        Invoice saved = service.create(invoice);
        ra.addFlashAttribute("success", "Invoice " + saved.getInvoiceNumber() + " generated successfully.");
        return "redirect:/invoices";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("invoice", service.findById(id));
        model.addAttribute("edit", true);
        loadDropdowns(model);
        return "invoices/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("invoice") Invoice invoice,
                         BindingResult result, Model model, RedirectAttributes ra) {
        validate(invoice, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            loadDropdowns(model);
            return "invoices/form";
        }
        service.update(id, invoice);
        ra.addFlashAttribute("success", "Invoice updated successfully.");
        return "redirect:/invoices";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Invoice deleted successfully.");
        return "redirect:/invoices";
    }

    /** Invoice detail page - also shows and accepts payments. */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Invoice invoice = service.findById(id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("payments", paymentService.findByInvoice(id));
        model.addAttribute("paid", service.paidAmount(id));
        model.addAttribute("balance", service.balance(invoice));
        if (!model.containsAttribute("payment")) {
            Payment p = new Payment();
            p.setInvoice(invoice);
            model.addAttribute("payment", p);
        }
        model.addAttribute("methods", Payment.Method.values());
        return "invoices/view";
    }

    /** Record a payment against this invoice. */
    @PostMapping("/{id}/payments")
    public String addPayment(@PathVariable Long id,
                             @Valid @ModelAttribute("payment") Payment payment,
                             BindingResult result, Model model, RedirectAttributes ra) {
        Invoice invoice = service.findById(id);
        payment.setInvoice(invoice);

        BigDecimal balance = service.balance(invoice);
        if (payment.getAmount() != null && payment.getAmount().compareTo(balance) > 0) {
            result.rejectValue("amount", "amount.tooBig",
                    "Payment cannot be more than the outstanding balance (LKR " + balance + ")");
        }
        if (invoice.getStatus() == Invoice.Status.CANCELLED) {
            result.reject("invoice.cancelled", "Payments cannot be added to a cancelled invoice");
        }

        if (result.hasErrors()) {
            model.addAttribute("invoice", invoice);
            model.addAttribute("payments", paymentService.findByInvoice(id));
            model.addAttribute("paid", service.paidAmount(id));
            model.addAttribute("balance", balance);
            model.addAttribute("methods", Payment.Method.values());
            return "invoices/view";
        }

        paymentService.save(payment);
        service.refreshStatus(invoice);
        ra.addFlashAttribute("success", "Payment recorded successfully.");
        return "redirect:/invoices/view/" + id;
    }

    @GetMapping("/{id}/payments/delete/{paymentId}")
    public String deletePayment(@PathVariable Long id, @PathVariable Long paymentId,
                                RedirectAttributes ra) {
        paymentService.delete(paymentId);
        service.refreshStatus(service.findById(id));
        ra.addFlashAttribute("success", "Payment record deleted.");
        return "redirect:/invoices/view/" + id;
    }

    /** Printable invoice (use the browser's Print > Save as PDF). */
    @GetMapping("/print/{id}")
    public String print(@PathVariable Long id, Model model) {
        Invoice invoice = service.findById(id);
        model.addAttribute("invoice", invoice);
        model.addAttribute("payments", paymentService.findByInvoice(id));
        model.addAttribute("paid", service.paidAmount(id));
        model.addAttribute("balance", service.balance(invoice));
        return "invoices/print";
    }

    private void validate(Invoice i, BindingResult result) {
        if (i.getIssueDate() != null && i.getDueDate() != null
                && i.getDueDate().isBefore(i.getIssueDate())) {
            result.rejectValue("dueDate", "date.order", "Due date cannot be before the issue date");
        }
        if (i.getCampaign() != null && i.getClient() != null
                && i.getCampaign().getClient() != null
                && !i.getCampaign().getClient().getId().equals(i.getClient().getId())) {
            result.rejectValue("campaign", "campaign.client",
                    "The selected campaign does not belong to the selected client");
        }
    }
}
