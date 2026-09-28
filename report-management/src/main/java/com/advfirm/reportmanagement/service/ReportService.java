package com.advfirm.reportmanagement.service;

import com.advfirm.reportmanagement.model.Report;
import com.advfirm.reportmanagement.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    private final String UPLOAD_DIR = "uploads/reports/";

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public List<Report> getReportsByUser(String username) {
        return reportRepository.findByCreatedBy(username);
    }

    public Report createReport(Report report, MultipartFile file, String username) throws IOException {
        report.setCreatedBy(username);
        report.setStatus("PENDING");
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            saveFile(report, file);
        }
        return reportRepository.save(report);
    }

    public Report updateReport(Long id, Report updatedData, MultipartFile file) throws IOException {
        Report existing = reportRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Report not found with id: " + id));

        existing.setTitle(updatedData.getTitle());
        existing.setDescription(updatedData.getDescription());
        existing.setCategory(updatedData.getCategory());
        existing.setUpdatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            saveFile(existing, file);
        }
        return reportRepository.save(existing);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    public Report updateStatus(Long id, String status) {
        Report report = reportRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Report not found"));
        report.setStatus(status); // APPROVED / REJECTED
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    private void saveFile(Report report, MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + uniqueName);
        Files.write(filePath, file.getBytes());

        report.setFilePath(filePath.toString());
        report.setFileName(file.getOriginalFilename());
    }
}
