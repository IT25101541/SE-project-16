package com.creativepulse.dto;

import com.creativepulse.entity.enums.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AdvertisementVersionDto {

    private Long id;

    private Integer versionNumber; // Read-only

    @NotBlank(message = "Asset URL or file link is required")
    @Size(max = 500, message = "URL cannot exceed 500 characters")
    private String assetUrl;

    private String changelogNotes;

    private String clientFeedback;

    private ReviewStatus reviewStatus;

    private LocalDateTime submittedAt;
    
    private LocalDateTime reviewedAt;

    private Long advertisementId;

    private Long createdById;
    private String createdByName; // Read-only

    public AdvertisementVersionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public String getAssetUrl() { return assetUrl; }
    public void setAssetUrl(String assetUrl) { this.assetUrl = assetUrl; }

    public String getChangelogNotes() { return changelogNotes; }
    public void setChangelogNotes(String changelogNotes) { this.changelogNotes = changelogNotes; }

    public String getClientFeedback() { return clientFeedback; }
    public void setClientFeedback(String clientFeedback) { this.clientFeedback = clientFeedback; }

    public ReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(ReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public Long getAdvertisementId() { return advertisementId; }
    public void setAdvertisementId(Long advertisementId) { this.advertisementId = advertisementId; }

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
}
