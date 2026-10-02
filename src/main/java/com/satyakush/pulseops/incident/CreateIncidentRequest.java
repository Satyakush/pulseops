package com.satyakush.pulseops.incident;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIncidentRequest(
        @NotNull(message = "serviceId is required") UUID serviceId,
        @NotBlank(message = "title is required") @Size(max = 150, message = "title must be at most 150 characters") String title,
        @Size(max = 1000, message = "description must be at most 1000 characters") String description,
        @NotNull(message = "severity is required") IncidentSeverity severity
) {
}
