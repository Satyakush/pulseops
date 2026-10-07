package com.satyakush.pulseops.api;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthResponseTest {

    @Test
    void healthResponseKeepsStableServiceIdentity() {
        HealthResponse response = new HealthResponse(
                "UP", "pulseops", OffsetDateTime.now(), "req-7");

        assertEquals("UP", response.status());
        assertEquals("pulseops", response.service());
        assertEquals("req-7", response.requestId());
    }
}
