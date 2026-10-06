package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;

/**
 * Concrete State – REJECTED
 * Designer can upload a revised version (moves it back toward DRAFT).
 */
public class RejectedState implements DesignState {

    @Override
    public void submit(Advertisement ad) {
        // After fixing the design the designer can submit again
        ad.setStatus(Advertisement.Status.SUBMITTED);
    }

    @Override
    public void approve(Advertisement ad) {
        throw new IllegalStateException(
                "Cannot approve a REJECTED design. Upload a new version first.");
    }

    @Override
    public void reject(Advertisement ad) {
        throw new IllegalStateException("Design is already REJECTED.");
    }

    @Override
    public void uploadNewVersion(Advertisement ad) {
        // Allowed – new version after rejection
        ad.setVersion(ad.getVersion() + 1);
        ad.setStatus(Advertisement.Status.DRAFT); // back to draft for further work
    }

    @Override
    public String getName() {
        return "REJECTED";
    }
}
