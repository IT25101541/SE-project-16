package com.creativepulse.pattern.campaign;

import com.creativepulse.model.Campaign;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject (Observable) for Campaign events.
 * CampaignService can call notifyObservers(...) after creating or updating a campaign.
 *
 * Compatible with creativepulse 10-3.
 */
@Component
public class CampaignSubject {

    private final List<CampaignObserver> observers = new ArrayList<>();

    public CampaignSubject(List<CampaignObserver> observers) {
        // Spring injects every bean that implements CampaignObserver
        this.observers.addAll(observers);
    }

    public void attach(CampaignObserver observer) {
        observers.add(observer);
    }

    public void detach(CampaignObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Campaign campaign, String eventType) {
        for (CampaignObserver observer : observers) {
            observer.onCampaignChanged(campaign, eventType);
        }
    }
}
