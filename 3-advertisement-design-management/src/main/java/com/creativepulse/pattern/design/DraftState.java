package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;

/**
 * Concrete State – DRAFT
 * Designer can still edit / upload new versions.
 * Only allowed transition is to SUBMITTED.
 */
public class DraftState implements DesignState {

    @Override
    public void submit(Advertisement ad) {
        ad.setStatus(Advertisement.Status.SUBMITTED);
    }

    @Override
    public void approve(Advertisement ad) {
        throw new IllegalStateException("Cannot approve a design that is still in DRAFT.");
    }

    @Override
    public void reject(Advertisement ad) {
        throw new IllegalStateException("Cannot reject a design that is still in DRAFT.");
    }

    @Override
    public void uploadNewVersion(Advertisement ad) {
        // Allowed – version increment is normally handled by AdvertisementService
        ad.setVersion(ad.getVersion() + 1);
    }

    @Override
    public String getName() {
        return "DRAFT";
    }
}
