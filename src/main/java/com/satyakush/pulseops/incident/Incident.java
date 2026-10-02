package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Incident(
        UUID id,
        UUID serviceId,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentStatus status,
        OffsetDateTime createdAt
) {
}
