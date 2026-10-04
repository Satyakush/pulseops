package com.satyakush.pulseops.realtime;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PulseOpsEvent(
        UUID id,
        String type,
        String resource,
        UUID resourceId,
        OffsetDateTime occurredAt
) {}
