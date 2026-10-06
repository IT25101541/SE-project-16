package com.creativepulse.repository;

import com.creativepulse.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByTitleContainingIgnoreCase(String title);
    List<Report> findByType(Report.ReportType type);
}
