package com.creativepulse.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ScheduleUpdate(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {
}
