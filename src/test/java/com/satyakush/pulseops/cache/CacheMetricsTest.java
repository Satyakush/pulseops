package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheMetricsTest {
    @Test
    void recordsInvalidations() {
        CacheMetrics metrics = new CacheMetrics();

        metrics.recordInvalidation();
        metrics.recordInvalidation();

        assertEquals(2, metrics.invalidations());
    }

    @Test void invalidationCountIncreasesByOnePerCall() { CacheMetrics metrics = new CacheMetrics(); metrics.recordInvalidation(); assertEquals(1, metrics.invalidations()); metrics.recordInvalidation(); assertEquals(2, metrics.invalidations()); }
}
