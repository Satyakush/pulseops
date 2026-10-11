package com.satyakush.pulseops.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiMediaTypesTest {
    @Test
    void definesStableV1MediaType() {
        assertEquals("application/vnd.pulseops.v1+json", ApiMediaTypes.V1);
    }

    @Test
    void versionedMediaTypeUsesVendorJsonSyntax() {
        org.junit.jupiter.api.Assertions.assertTrue(ApiMediaTypes.V1.startsWith("application/vnd."));
        org.junit.jupiter.api.Assertions.assertTrue(ApiMediaTypes.V1.endsWith("+json"));
    }
}
