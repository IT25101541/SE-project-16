package com.creativepulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Report Management module (Major Function 6).
 * Supports full CRUD: generate (Create), view/filter (Read),
 * regenerate/rename (Update) and delete (Delete). Also exports to CSV.
 */
@Entity
@Table(name = "reports")
public class Report {

    public enum ReportType { CAMPAIGN_PERFORMANCE, CLIENT_SUMMARY, FINANCIAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Report title is required")
    @Size(min = 3, max = 100, message = "Report title must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @NotNull(message = "Please select a report type")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportType type;

    @NotNull(message = "Period 'from' date is required")
    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodFrom;

    @NotNull(message = "Period 'to' date is required")
    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodTo;

    @Size(max = 300, message = "Notes cannot exceed 300 characters")
    @Column(length = 300)
    private String notes;

    /** The generated report body, produced by the Strategy classes. */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(length = 60)
    private String generatedBy;

    @Column(nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }
    public LocalDate getPeriodFrom() { return periodFrom; }
    public void setPeriodFrom(LocalDate periodFrom) { this.periodFrom = periodFrom; }
    public LocalDate getPeriodTo() { return periodTo; }
    public void setPeriodTo(LocalDate periodTo) { this.periodTo = periodTo; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
