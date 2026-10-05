package com.creativepulse.dto;

import com.creativepulse.entity.enums.AdFormat;
import com.creativepulse.entity.enums.AdStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AdvertisementDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Format is required")
    private AdFormat format;

    private AdStatus status;

    @NotNull(message = "Campaign is required")
    private Long campaignId;

    private String campaignName; // Read-only

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdvertisementDto() {}

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

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }

    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
