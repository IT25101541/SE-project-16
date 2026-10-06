package com.creativepulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
public class Advertisement {

    public enum AdType { BANNER, VIDEO, PRINT, SOCIAL_POST, RADIO }
    public enum Status { DRAFT, SUBMITTED, APPROVED, REJECTED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Advertisement title is required")
    @Size(min = 3, max = 80, message = "Title must be between 3 and 80 characters")
    @Column(nullable = false, length = 80)
    private String title;

    @NotNull(message = "Please select the campaign")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @NotNull(message = "Please select the advertisement type")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdType type;

    @NotNull(message = "Please select a status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.DRAFT;

    @Min(value = 1, message = "Version must be 1 or higher")
    @Column(nullable = false)
    private int version = 1;

    /** Original file name of the uploaded design. */
    @Column(length = 200)
    private String fileName;

    /** Name of the file as it is stored on disk (uploads folder). */
    @Column(length = 200)
    private String storedFileName;

    @Size(max = 400, message = "Notes cannot exceed 400 characters")
    @Column(length = 400)
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }
    public AdType getType() { return type; }
    public void setType(AdType type) { this.type = type; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getStoredFileName() { return storedFileName; }
    public void setStoredFileName(String storedFileName) { this.storedFileName = storedFileName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
