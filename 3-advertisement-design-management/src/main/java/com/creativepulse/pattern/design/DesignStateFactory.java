package com.creativepulse.pattern.design;

import com.creativepulse.model.Advertisement;
import org.springframework.stereotype.Component;

/**
 * Helper that returns the correct DesignState object
 * for the current status of an Advertisement.
 *
 * Compatible with creativepulse 10-3 status enum.
 */
@Component
public class DesignStateFactory {

    public DesignState fromStatus(Advertisement.Status status) {
        return switch (status) {
            case DRAFT     -> new DraftState();
            case SUBMITTED -> new SubmittedState();
            case APPROVED  -> new ApprovedState();
            case REJECTED  -> new RejectedState();
        };
    }
}
