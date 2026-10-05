package com.creativepulse.entity;

import com.creativepulse.entity.enums.AdFormat;
import com.creativepulse.entity.enums.AdStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single advertisement creative within a campaign.
 *
 * An Advertisement tracks the creative concept and format.
 * The actual creative files / asset history is tracked in AdvertisementVersion.
 * Lifecycle: DRAFT → IN_PRODUCTION → PENDING_CLIENT_APPROVAL → APPROVED / REJECTED → PUBLISHED
 */
@Entity
@Table(name = "advertisements")
public class Advertisement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false, length = 30)
    private AdFormat format;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AdStatus status = AdStatus.DRAFT;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_advertisements_campaign"))
    private Campaign campaign;

    /**
     * Ordered list of asset versions for this advertisement.
     * All versions are cascade-deleted when this advertisement is deleted.
     */
    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("versionNumber DESC")
    private List<AdvertisementVersion> versions = new ArrayList<>();

    public Advertisement() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AdFormat getFormat() { return format; }
    public void setFormat(AdFormat format) { this.format = format; }

    public AdStatus getStatus() { return status; }
    public void setStatus(AdStatus status) { this.status = status; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public List<AdvertisementVersion> getVersions() { return versions; }
    public void setVersions(List<AdvertisementVersion> versions) { this.versions = versions; }
}
