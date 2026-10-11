package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PulseOpsCachePropertiesTest {
    @Test
    void defaultsToSixtySecondTtl() {
        assertEquals(Duration.ofSeconds(60), new PulseOpsCacheProperties().getTtl());
    }

    @Test void configuredTtlRoundTrips() { var properties = new PulseOpsCacheProperties(); var ttl = Duration.ofMinutes(2); properties.setTtl(ttl); assertEquals(ttl, properties.getTtl()); }
}
