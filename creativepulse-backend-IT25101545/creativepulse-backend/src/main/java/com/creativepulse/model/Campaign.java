package com.creativepulse.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Campaign(
        Integer campaignId,
        @NotNull(message = "clientId is required") Integer clientId,
        String clientName,
        @NotBlank(message = "Campaign name is required") String name,
        String description,
        @NotNull @PositiveOrZero(message = "Budget cannot be negative") BigDecimal budget,
        @NotNull(message = "Start date is required") LocalDate startDate,
        @NotNull(message = "End date is required") LocalDate endDate,
        @Pattern(regexp = "PLANNED|IN_PROGRESS|ON_HOLD|COMPLETED|CANCELLED",
                 message = "Invalid status") String status,
        @Min(0) @Max(100) Integer progress) {
}
