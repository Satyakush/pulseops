package com.satyakush.pulseops.incident;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Immutable audit entry describing a meaningful change to an incident. */
public record IncidentActivity(
        UUID id,
        UUID incidentId,
        IncidentActivityAction action,
        String actor,
        String summary,
        OffsetDateTime occurredAt
) {
}
