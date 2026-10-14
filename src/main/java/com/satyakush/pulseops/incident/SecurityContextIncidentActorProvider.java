package com.satyakush.pulseops.incident;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextIncidentActorProvider implements IncidentActorProvider {
    private static final String SYSTEM_ACTOR = "system";

    @Override
    public String currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return SYSTEM_ACTOR;
        }
        String name = authentication.getName();
        if (name == null || name.isBlank() || "anonymousUser".equals(name)) {
            return SYSTEM_ACTOR;
        }
        return name;
    }
}
