package com.satyakush.pulseops.cache;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheMetricsEndpoint {
    private final CacheMetrics metrics;

    public CacheMetricsEndpoint(CacheMetrics metrics) {
        this.metrics = metrics;
    }

    @GetMapping("/metrics")
    public Map<String, Long> metrics() {
        return Map.of("invalidations", metrics.invalidations());
    }
}
