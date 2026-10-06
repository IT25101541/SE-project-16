package com.creativepulse.service;

import com.creativepulse.model.Report;
import com.creativepulse.pattern.ReportStrategyFactory;
import com.creativepulse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Report Management (Major Function 6) - full CRUD plus regeneration and export.
 * Uses the Factory + Strategy patterns to build the report content.
 */
@Service
public class ReportService {

    private final ReportRepository repo;
    private final ReportStrategyFactory factory;

    public ReportService(ReportRepository repo, ReportStrategyFactory factory) {
        this.repo = repo;
        this.factory = factory;
    }

    public List<Report> search(String keyword, Report.ReportType type) {
        List<Report> result = (keyword == null || keyword.isBlank())
                ? repo.findAll()
                : repo.findByTitleContainingIgnoreCase(keyword);
        if (type != null) {
            result = result.stream().filter(r -> r.getType() == type).toList();
        }
        return result;
    }

    public List<Report> findAll() { return repo.findAll(); }

    public Report findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id " + id));
    }

    /** CREATE - generates the content using the matching strategy. */
    public Report generate(Report report, String username) {
        report.setGeneratedBy(username);
        report.setContent(factory.getStrategy(report.getType()).build(report));
        return repo.save(report);
    }

    /** UPDATE - saves the edited details and rebuilds the content. */
    public Report update(Long id, Report form, String username) {
        Report db = findById(id);
        db.setTitle(form.getTitle());
        db.setType(form.getType());
        db.setPeriodFrom(form.getPeriodFrom());
        db.setPeriodTo(form.getPeriodTo());
        db.setNotes(form.getNotes());
        db.setGeneratedBy(username);
        db.setContent(factory.getStrategy(db.getType()).build(db));
        return repo.save(db);
    }

    /** DELETE */
    public void delete(Long id) { repo.deleteById(id); }

    public long count() { return repo.count(); }
}
