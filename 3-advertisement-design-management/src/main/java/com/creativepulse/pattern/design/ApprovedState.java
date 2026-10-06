package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;

/**
 * Concrete State – APPROVED
 * Final state for a successful design. Almost no further changes allowed.
 */
public class ApprovedState implements DesignState {

    @Override
    public void submit(Advertisement ad) {
        throw new IllegalStateException("Design is already APPROVED.");
    }

    @Override
    public void approve(Advertisement ad) {
        throw new IllegalStateException("Design is already APPROVED.");
    }

    @Override
    public void reject(Advertisement ad) {
        throw new IllegalStateException("Cannot reject an already APPROVED design.");
    }

    @Override
    public void uploadNewVersion(Advertisement ad) {
        throw new IllegalStateException(
                "Cannot change an APPROVED design. Create a new advertisement instead.");
    }

    @Override
    public String getName() {
        return "APPROVED";
    }
}
