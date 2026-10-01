package com.satyakush.pulseops.service;

import jakarta.validation.constraints.NotNull;

public record UpdateServiceStatusRequest(
        @NotNull(message = "status is required")
        ServiceStatus status
) {
}
