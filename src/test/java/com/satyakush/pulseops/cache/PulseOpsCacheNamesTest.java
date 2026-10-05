package com.satyakush.pulseops.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PulseOpsCacheNamesTest {
    @Test
    void exposesStableCacheNames() {
        assertEquals("services", PulseOpsCacheNames.SERVICES);
        assertEquals("incidents", PulseOpsCacheNames.INCIDENTS);
    }
}
