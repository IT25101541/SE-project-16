package com.advfirm.reportmanagement.repository;

import com.advfirm.reportmanagement.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCreatedBy(String createdBy);
    List<Report> findByStatus(String status);
    List<Report> findByCategory(String category);
}
