package com.creativepulse.pattern.campaign;

import com.creativepulse.model.Campaign;

/**
 * DESIGN PATTERN – OBSERVER (Campaign Management)
 *
 * Any class that wants to react to campaign lifecycle events
 * (created, status changed, team assigned) implements this interface.
 *
 * Compatible with creativepulse 10-3.
 */
public interface CampaignObserver {

    /**
     * Called whenever a significant campaign event occurs.
     *
     * @param campaign  the campaign that changed
     * @param eventType a short string describing the event
     *                  (e.g. "CREATED", "STATUS_CHANGED", "TEAM_ASSIGNED")
     */
    void onCampaignChanged(Campaign campaign, String eventType);
}
