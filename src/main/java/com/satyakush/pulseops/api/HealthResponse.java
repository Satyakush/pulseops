package com.satyakush.pulseops.api;

import java.time.OffsetDateTime;

public record HealthResponse(
        String status,
        String service,
        OffsetDateTime timestamp,
        String requestId
) {
}
