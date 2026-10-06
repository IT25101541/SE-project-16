package com.creativepulse.controller;

import com.creativepulse.model.Report;
import com.creativepulse.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;

/**
 * MAJOR FUNCTION 6 - Report Management (System Administrator / Management).
 * FULL CRUD:  Create (generate) | Read (list, filter, view) | Update (edit + regenerate) | Delete
 * Plus CSV/Excel export and a printable view for PDF.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) { this.service = service; }

    @ModelAttribute("types")
    public Report.ReportType[] types() { return Report.ReportType.values(); }

    /** READ - list with search + type filter */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Report.ReportType type,
                       Model model) {
        model.addAttribute("reports", service.search(keyword, type));
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedType", type);
        return "reports/list";
    }

    /** CREATE - blank form */
    @GetMapping("/new")
    public String createForm(Model model) {
        Report report = new Report();
        report.setPeriodFrom(LocalDate.now().withDayOfMonth(1));
        report.setPeriodTo(LocalDate.now());
        model.addAttribute("report", report);
        model.addAttribute("edit", false);
        return "reports/form";
    }

    /** CREATE - generate and save */
    @PostMapping("/save")
    public String create(@Valid @ModelAttribute("report") Report report, BindingResult result,
                        Principal principal, Model model, RedirectAttributes ra) {
        validatePeriod(report, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", false);
            return "reports/form";
        }
        service.generate(report, principal.getName());
        ra.addFlashAttribute("success", "Report \"" + report.getTitle() + "\" generated successfully.");
        return "redirect:/reports";
    }

    /** READ - single report */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("report", service.findById(id));
        return "reports/view";
    }

    /** UPDATE - filled form */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("report", service.findById(id));
        model.addAttribute("edit", true);
        return "reports/form";
    }

    /** UPDATE - save changes and rebuild the content */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("report") Report report,
                         BindingResult result, Principal principal,
                         Model model, RedirectAttributes ra) {
        validatePeriod(report, result);
        if (result.hasErrors()) {
            model.addAttribute("edit", true);
            return "reports/form";
        }
        service.update(id, report, principal.getName());
        ra.addFlashAttribute("success", "Report updated and regenerated successfully.");
        return "redirect:/reports";
    }

    /** DELETE */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Report deleted successfully.");
        return "redirect:/reports";
    }

    /** EXPORT - downloads a .csv file that opens directly in Excel. */
    @GetMapping("/export/{id}")
    public ResponseEntity<ByteArrayResource> export(@PathVariable Long id) {
        Report report = service.findById(id);
        String body = report.getContent() == null ? "" : report.getContent();
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        String fileName = report.getTitle().replaceAll("[^A-Za-z0-9]+", "_") + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    /** Printable view - use the browser Print dialog and "Save as PDF". */
    @GetMapping("/print/{id}")
    public String print(@PathVariable Long id, Model model) {
        model.addAttribute("report", service.findById(id));
        return "reports/print";
    }

    private void validatePeriod(Report r, BindingResult result) {
        if (r.getPeriodFrom() != null && r.getPeriodTo() != null
                && r.getPeriodTo().isBefore(r.getPeriodFrom())) {
            result.rejectValue("periodTo", "period.order",
                    "The 'to' date cannot be before the 'from' date");
        }
        if (r.getPeriodFrom() != null && r.getPeriodFrom().isAfter(LocalDate.now())) {
            result.rejectValue("periodFrom", "period.future",
                    "The 'from' date cannot be in the future");
        }
    }
}
