package com.creativepulse.usermanagement.campaign.dto;

import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignCategory;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Used for both creating and editing a campaign. */
public class CampaignForm {

    @NotBlank(message = "Campaign name is required")
    @Size(max = 120, message = "Campaign name must be at most 120 characters")
    private String name;

    @NotBlank(message = "Client name is required")
    @Size(max = 120, message = "Client name must be at most 120 characters")
    private String clientName;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Please select a category")
    private CampaignCategory category;

    @NotNull(message = "Please select a status")
    private CampaignStatus status = CampaignStatus.PLANNING;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.00", message = "Budget cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Budget can have at most 10 digits and 2 decimals")
    private BigDecimal budget;

    @NotNull(message = "Progress is required")
    @Min(value = 0, message = "Progress must be between 0 and 100")
    @Max(value = 100, message = "Progress must be between 0 and 100")
    private Integer progress = 0;

    /** Optional: id of the team member the campaign is assigned to. */
    private Long assignedToId;

    public static CampaignForm from(Campaign campaign) {
        CampaignForm form = new CampaignForm();
        form.setName(campaign.getName());
        form.setClientName(campaign.getClientName());
        form.setDescription(campaign.getDescription());
        form.setCategory(campaign.getCategory());
        form.setStatus(campaign.getStatus());
        form.setStartDate(campaign.getStartDate());
        form.setEndDate(campaign.getEndDate());
        form.setBudget(campaign.getBudget());
        form.setProgress(campaign.getProgress());
        form.setAssignedToId(campaign.getAssignedToId());
        return form;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CampaignCategory getCategory() { return category; }
    public void setCategory(CampaignCategory category) { this.category = category; }

    public CampaignStatus getStatus() { return status; }
    public void setStatus(CampaignStatus status) { this.status = status; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }
}
