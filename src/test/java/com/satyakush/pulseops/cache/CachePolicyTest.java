package com.satyakush.pulseops.cache;

import com.satyakush.pulseops.incident.IncidentService;
import com.satyakush.pulseops.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CachePolicyTest {
    @Test
    void serviceReadsAreCacheable() throws Exception {
        assertNotNull(ServiceService.class.getMethod("findAll").getAnnotation(Cacheable.class));
        assertNotNull(ServiceService.class.getMethod("getSummary").getAnnotation(Cacheable.class));
    }

    @Test
    void incidentReadsAreCacheable() throws Exception {
        assertNotNull(IncidentService.class.getMethod("findAll").getAnnotation(Cacheable.class));
        assertNotNull(IncidentService.class.getMethod("getSummary").getAnnotation(Cacheable.class));
    }

    @Test
    void mutationsEvictServiceAndIncidentCaches() throws Exception {
        assertNotNull(ServiceService.class.getMethod("create", String.class, String.class).getAnnotation(CacheEvict.class));
        assertNotNull(IncidentService.class.getMethod("create", com.satyakush.pulseops.incident.CreateIncidentRequest.class).getAnnotation(CacheEvict.class));
    }
}
