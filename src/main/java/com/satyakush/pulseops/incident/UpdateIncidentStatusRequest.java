package com.satyakush.pulseops.incident;

import com.satyakush.pulseops.api.ValidationMessages;
import jakarta.validation.constraints.NotNull;

public record UpdateIncidentStatusRequest(
        @NotNull(message = ValidationMessages.REQUIRED_STATUS)
        IncidentStatus status
) {
}
