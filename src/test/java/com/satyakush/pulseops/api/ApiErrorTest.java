package com.satyakush.pulseops.api;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiErrorTest {

    @Test
    void exposesCorrelationIdAlongsideErrorDetails() {
        ApiError error = new ApiError(
                OffsetDateTime.now(),
                404,
                "Not Found",
                "missing",
                "/api/v1/services/1",
                "req-42"
        );

        assertEquals("req-42", error.requestId());
        assertEquals(404, error.status());
    }
}
