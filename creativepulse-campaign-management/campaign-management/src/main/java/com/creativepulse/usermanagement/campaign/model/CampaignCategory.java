package com.creativepulse.usermanagement.campaign.model;

public enum CampaignCategory {

    DIGITAL("Digital"),
    SOCIAL_MEDIA("Social media"),
    PRINT("Print"),
    TELEVISION("Television"),
    RADIO("Radio"),
    OUTDOOR("Outdoor / billboard");

    private final String displayName;

    CampaignCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
