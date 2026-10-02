package com.satyakush.pulseops.service;

public record ServiceSummary(
        long total,
        long operational,
        long degraded,
        long outage
) {
}
