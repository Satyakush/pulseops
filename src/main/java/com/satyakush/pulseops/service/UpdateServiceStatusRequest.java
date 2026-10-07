package com.satyakush.pulseops.service;

import com.satyakush.pulseops.api.ValidationMessages;
import jakarta.validation.constraints.NotNull;

public record UpdateServiceStatusRequest(
        @NotNull(message = ValidationMessages.REQUIRED_STATUS)
        ServiceStatus status
) {
}
