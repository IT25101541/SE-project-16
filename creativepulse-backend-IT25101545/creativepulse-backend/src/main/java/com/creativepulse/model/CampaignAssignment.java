package com.creativepulse.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CampaignAssignment(
        Integer assignmentId,
        Integer campaignId,
        @NotNull(message = "userId is required") Integer userId,
        String userName,
        String roleInTeam,
        LocalDateTime assignedAt) {
}
