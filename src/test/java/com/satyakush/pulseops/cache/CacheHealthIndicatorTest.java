package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheHealthIndicatorTest {
    @Test
    void reportsCacheHealthAndInvalidationCount() {
        CacheMetrics metrics = new CacheMetrics();
        metrics.recordInvalidation();
        CacheHealthIndicator indicator = new CacheHealthIndicator(metrics);

        var health = indicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals(1L, health.getDetails().get("invalidationCount"));
    }
}
