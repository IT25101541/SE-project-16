package com.creativepulse.entity;

import com.creativepulse.entity.enums.ReviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Tracks individual revisions of an Advertisement creative asset.
 *
 * Each time an employee uploads a new asset file, a new version record is created,
 * preserving a full history of revisions and client feedback.
 * The active/latest version is determined by the highest versionNumber.
 */
@Entity
@Table(name = "advertisement_versions")
public class AdvertisementVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_ad_versions_advertisement"))
    private Advertisement advertisement;

    @NotNull
    @Min(1)
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @NotBlank
    @Column(name = "asset_url", nullable = false, length = 500)
    private String assetUrl;

    @Column(name = "changelog_notes", columnDefinition = "TEXT")
    private String changelogNotes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false, length = 25)
    private ReviewStatus reviewStatus = ReviewStatus.SUBMITTED;

    @Column(name = "client_feedback", columnDefinition = "TEXT")
    private String clientFeedback;

    /** User (employee/designer) who uploaded this version. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id",
                foreignKey = @ForeignKey(name = "fk_ad_versions_created_by"))
    private User createdBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public AdvertisementVersion() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Advertisement getAdvertisement() { return advertisement; }
    public void setAdvertisement(Advertisement advertisement) { this.advertisement = advertisement; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public String getAssetUrl() { return assetUrl; }
    public void setAssetUrl(String assetUrl) { this.assetUrl = assetUrl; }

    public String getChangelogNotes() { return changelogNotes; }
    public void setChangelogNotes(String changelogNotes) { this.changelogNotes = changelogNotes; }

    public ReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(ReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }

    public String getClientFeedback() { return clientFeedback; }
    public void setClientFeedback(String clientFeedback) { this.clientFeedback = clientFeedback; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
