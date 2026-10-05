package com.satyakush.pulseops.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CacheMetrics {
    private final AtomicLong invalidations = new AtomicLong();

    public void recordInvalidation() {
        invalidations.incrementAndGet();
    }

    public long invalidations() {
        return invalidations.get();
    }
}
