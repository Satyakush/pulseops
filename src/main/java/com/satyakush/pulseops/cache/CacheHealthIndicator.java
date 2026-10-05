package com.satyakush.pulseops.cache;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CacheHealthIndicator implements HealthIndicator {
    private final CacheMetrics metrics;

    public CacheHealthIndicator(CacheMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("invalidationCount", metrics.invalidations())
                .build();
    }
}
