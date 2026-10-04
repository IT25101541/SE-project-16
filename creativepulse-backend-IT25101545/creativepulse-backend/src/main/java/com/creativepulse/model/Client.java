package com.creativepulse.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public record Client(
        Integer clientId,
        @NotBlank(message = "Company name is required") String companyName,
        @NotBlank(message = "Contact person is required") String contactPerson,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email") String email,
        @Pattern(regexp = "^[0-9+\\- ]{7,20}$", message = "Invalid phone number") String phone,
        String address,
        String industry,
        @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE") String status,
        LocalDateTime createdAt) {
}
