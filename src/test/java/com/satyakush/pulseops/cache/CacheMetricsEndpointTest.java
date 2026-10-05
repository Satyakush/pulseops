package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheMetricsEndpointTest {
    @Test
    void exposesInvalidationCount() {
        CacheMetrics metrics = new CacheMetrics();
        metrics.recordInvalidation();

        var response = new CacheMetricsEndpoint(metrics).metrics();

        assertEquals(1L, response.get("invalidations"));
    }
}
