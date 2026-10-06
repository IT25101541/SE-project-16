package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;

/**
 * Concrete State – SUBMITTED
 * Waiting for client review. Only approve or reject are allowed.
 */
public class SubmittedState implements DesignState {

    @Override
    public void submit(Advertisement ad) {
        throw new IllegalStateException("Design is already SUBMITTED.");
    }

    @Override
    public void approve(Advertisement ad) {
        ad.setStatus(Advertisement.Status.APPROVED);
    }

    @Override
    public void reject(Advertisement ad) {
        ad.setStatus(Advertisement.Status.REJECTED);
    }

    @Override
    public void uploadNewVersion(Advertisement ad) {
        throw new IllegalStateException(
                "Cannot upload a new version while the design is under review (SUBMITTED).");
    }

    @Override
    public String getName() {
        return "SUBMITTED";
    }
}
