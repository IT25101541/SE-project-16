package com.creativepulse.pattern.campaign;

import com.creativepulse.model.Campaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer – reacts when a campaign changes.
 * Currently logs the event so the behaviour is visible.
 * You can later replace the log with real email / in-app notifications.
 *
 * Compatible with creativepulse 10-3.
 */
@Component
public class CampaignNotificationObserver implements CampaignObserver {

    private static final Logger log = LoggerFactory.getLogger(CampaignNotificationObserver.class);

    @Override
    public void onCampaignChanged(Campaign campaign, String eventType) {
        log.info("[CampaignObserver] Event={} | CampaignId={} | Name={} | Status={}",
                eventType,
                campaign.getId(),
                campaign.getName(),
                campaign.getStatus());

        // Future extension points:
        // - notify assigned designers / employees
        // - notify the client that a new campaign has been created
        // - push an in-app notification to the dashboard
    }
}
