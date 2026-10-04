package com.creativepulse.model;

import jakarta.validation.constraints.*;

public record StatusUpdate(
        @NotBlank @Pattern(regexp = "PLANNED|IN_PROGRESS|ON_HOLD|COMPLETED|CANCELLED") String status,
        @NotNull @Min(0) @Max(100) Integer progress) {
}
