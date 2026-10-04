package com.satyakush.pulseops.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDetailsServiceTest {
    private final PulseOpsSecurityProperties properties = configuredProperties();
    private final PulseOpsUserDetailsService service = new PulseOpsUserDetailsService(properties);

    @Test
    void knownUserLoadsExpectedRole() {
        var user = service.loadUserByUsername("operator");
        assertEquals("operator", user.getUsername());
        assertEquals("ROLE_OPERATOR", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void unknownUserIsRejected() {
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing"));
    }

    private static PulseOpsSecurityProperties configuredProperties() {
        var properties = new PulseOpsSecurityProperties();
        var operator = new PulseOpsSecurityProperties.User();
        operator.setUsername("operator");
        operator.setPassword("{noop}operator");
        operator.setRole(PulseOpsRole.OPERATOR);
        properties.setUsers(List.of(operator));
        return properties;
    }
}
