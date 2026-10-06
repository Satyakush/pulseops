package com.satyakush.pulseops.api;

import java.time.OffsetDateTime;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/v1/health")
    public HealthResponse health(HttpServletRequest request) {
        Object requestId = request.getAttribute(RequestIdFilter.ATTRIBUTE);
        return new HealthResponse(
                "UP",
                "pulseops",
                OffsetDateTime.now(),
                requestId == null ? null : requestId.toString()
        );
    }
}
