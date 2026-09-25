package com.advfirm.reportmanagement.controller;

import com.advfirm.reportmanagement.model.Report;
import com.advfirm.reportmanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // View all reports for logged-in user (replace with real auth later)
    @GetMapping
    public String listReports(Model model, @RequestParam(defaultValue = "demoUser") String username) {
        List<Report> reports = reportService.getReportsByUser(username);
        model.addAttribute("reports", reports);
        return "reports/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("report", new Report());
        return "reports/form";
    }

    // Handle create
    @PostMapping("/save")
    public String createReport(@ModelAttribute Report report,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(defaultValue = "demoUser") String username) throws IOException {
        reportService.createReport(report, file, username);
        return "redirect:/reports";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("report", reportService.getReportById(id));
        return "reports/form";
    }

    // Handle update
    @PostMapping("/update/{id}")
    public String updateReport(@PathVariable Long id,
                                @ModelAttribute Report report,
                                @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        reportService.updateReport(id, report, file);
        return "redirect:/reports";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/reports";
    }

    // View single report
    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable Long id, Model model) {
        model.addAttribute("report", reportService.getReportById(id));
        return "reports/view";
    }

    // Download attachment
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws MalformedURLException {
        Report report = reportService.getReportById(id);
        Path path = Paths.get(report.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + report.getFileName() + "\"")
                .body(resource);
    }

    // ---- ADMIN ENDPOINTS ----

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "reports/admin";
    }

    @PostMapping("/admin/status/{id}")
    public String changeStatus(@PathVariable Long id, @RequestParam String status) {
        reportService.updateStatus(id, status);
        return "redirect:/reports/admin";
    }
}
