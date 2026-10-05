package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PulseOpsCachePropertiesTest {
    @Test
    void defaultsToSixtySecondTtl() {
        assertEquals(Duration.ofSeconds(60), new PulseOpsCacheProperties().getTtl());
    }
}
