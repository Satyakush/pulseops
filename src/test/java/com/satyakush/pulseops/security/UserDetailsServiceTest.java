package com.satyakush.pulseops.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDetailsServiceTest {
    private final PulseOpsUserDetailsService service = new PulseOpsUserDetailsService();

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
}
