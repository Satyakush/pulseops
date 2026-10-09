package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {
    @Test void issuesNonBlankTokenWithEightHourExpiry() {
        var before = OffsetDateTime.now();
        var token = new TokenService().issue(new User(UUID.randomUUID(), "viewer", "hash", AuthRole.VIEWER, UserStatus.ACTIVE));
        assertNotNull(token.value());
        assertFalse(token.value().isBlank());
        assertTrue(token.expiresAt().isAfter(before.plusHours(7).plusMinutes(59)));
        assertTrue(token.expiresAt().isBefore(before.plusHours(8).plusMinutes(1)));
    }

    @Test void refusesDisabledAccount() {
        var disabled = new User(UUID.randomUUID(), "disabled", "hash", AuthRole.VIEWER, UserStatus.DISABLED);
        assertThrows(IllegalArgumentException.class, () -> new TokenService().issue(disabled));
    }
}
