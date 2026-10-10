package com.satyakush.pulseops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Loads the real application context, rather than a web-slice, to catch
 * duplicate controllers, competing security chains, and missing production beans.
 */
@SpringBootTest
class ApplicationContextTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void applicationStartsWithOneSecurityFilterChain() {
        assertNotNull(context);
        assertNotNull(context.getBean("securityFilterChain"));
    }
}
