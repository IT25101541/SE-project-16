package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;

/**
 * DESIGN PATTERN – STATE (Advertisement Design Management)
 *
 * Represents one concrete state of an advertisement design
 * (Draft, Submitted, Approved, Rejected).
 * Each state decides which actions are legal and what the next state is.
 *
 * Compatible with creativepulse 10-3 status enum:
 * DRAFT, SUBMITTED, APPROVED, REJECTED
 */
public interface DesignState {

    void submit(Advertisement ad);

    void approve(Advertisement ad);

    void reject(Advertisement ad);

    /** Called when the designer uploads a new version of the file. */
    void uploadNewVersion(Advertisement ad);

    /** Human-readable name of this state (useful for logging / UI). */
    String getName();
}
