package com.satyakush.pulseops.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestIdHeaderContractTest {
    @Test
    void generatedHeaderUsesCanonicalName() {
        assertEquals("X-Request-Id", RequestIdFilter.HEADER);
    }
}
