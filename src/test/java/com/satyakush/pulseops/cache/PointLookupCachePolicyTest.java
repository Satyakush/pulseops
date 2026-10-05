package com.satyakush.pulseops.cache;

import com.satyakush.pulseops.incident.IncidentService;
import com.satyakush.pulseops.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PointLookupCachePolicyTest {
    @Test
    void servicePointLookupIsCacheable() throws Exception {
        var annotation = ServiceService.class.getMethod("findById", UUID.class).getAnnotation(Cacheable.class);
        assertNotNull(annotation);
    }

    @Test
    void incidentPointLookupIsCacheable() throws Exception {
        var annotation = IncidentService.class.getMethod("findById", UUID.class).getAnnotation(Cacheable.class);
        assertNotNull(annotation);
    }
}
