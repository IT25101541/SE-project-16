package com.creativepulse.usermanagement.campaign.model;

public enum CampaignStatus {

    PLANNING("Planning", "badge--planning"),
    ACTIVE("Active", "badge--active"),
    ON_HOLD("On hold", "badge--hold"),
    COMPLETED("Completed", "badge--done"),
    CANCELLED("Cancelled", "badge--cancelled");

    private final String displayName;
    private final String cssClass;

    CampaignStatus(String displayName, String cssClass) {
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssClass() {
        return cssClass;
    }
}
